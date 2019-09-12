package com.dandxy.strokes

import cats.Monad
import cats.implicits._
import com.dandxy.golf.entity.Location.{ OnTheGreen, TeeBox }
import com.dandxy.golf.entity.Par.ParThree
import com.dandxy.golf.entity.Score.findScore
import com.dandxy.golf.entity.{ Location, Par }
import com.dandxy.golf.input.GolfInput.UserShotInput
import com.dandxy.golf.input.{ Distance, Handicap, Strokes }
import com.dandxy.golf.pga.Statistic.PGAStatistic
import com.dandxy.model.user.Identifier.Hole
import com.dandxy.strokes.StablefordCalculator.{ calculate => StablefordPoints }
import com.dandxy.util.Helpers.{ combineAll, roundAt3 }

import scala.annotation.tailrec
import scala.language.higherKinds

object StrokesGainedCalculator {

  type GetStat[F[_]] = (Distance, Location) => F[Option[PGAStatistic]]

  def getMetrics[F[_]: Monad](dbOp: GetStat[F])(input: List[UserShotInput]): F[List[UserShotInput]] =
    input.map { v =>
      dbOp(v.distance, v.location).map {
        case None    => v
        case Some(s) => v.copy(strokesGained = Option(Strokes(s.strokes)))
      }
    }.sequence

  def calculateStrokesGained(a: Option[Strokes], b: Option[Strokes]): Option[Strokes] =
    a.map(v1 => b.fold(Strokes(roundAt3((v1.value - 0) - 1)))(v2 => Strokes(roundAt3((v1.value - v2.value) - 1))))

  @tailrec
  private[this] def tailRecResults(in: List[UserShotInput], out: List[UserShotInput]): List[UserShotInput] = in match {
    case Nil              => out
    case oneResult :: Nil => oneResult.copy(strokesGained = calculateStrokesGained(oneResult.strokesGained, None)) :: out
    case f :: s :: t      => tailRecResults(s :: t, f.copy(strokesGained = calculateStrokesGained(f.strokesGained, s.strokesGained)) :: out)
  }

  def getAllStrokesGained(in: List[UserShotInput]): List[UserShotInput] = tailRecResults(in, Nil).reverse

  def getStrokesGainedPutting(in: List[UserShotInput]): Option[Strokes] =
    combineAll(in.filter(_.location == OnTheGreen).map(_.strokesGained))

  def getStrokesGainedOffTheTee(in: List[UserShotInput], par: Par): Option[Strokes] =
    if (par == ParThree) Option(Strokes(0.0))
    else {
      in match {
        case head :: _ => head.strokesGained
        case _         => Option(Strokes(0.0))
      }
    }

  private[this] def filterApproachShots(in: List[UserShotInput]): List[UserShotInput] = in.filter { v =>
    Set(v.location).subsetOf(Location.approachLies)
  }

  def getStrokesGainedApproachTheGreen(in: List[UserShotInput], par: Par): Option[Strokes] = {
    val interim =
      if (par == ParThree) filterApproachShots(in)
      else filterApproachShots(in).filter(_.location != TeeBox)

    combineAll(interim.filter(v => v.distance.value > 30).map(_.strokesGained))
  }

  def getStrokesGainedAroundTheGreen(in: List[UserShotInput]): Option[Strokes] =
    combineAll(filterApproachShots(in).filter(v => v.distance.value <= 30).map(_.strokesGained))

  def countShots(input: List[UserShotInput]): Int =
    input.foldLeft[Int](0) { case (a, b) => a + b.location.shots }

  private def determineId(filterHole: Option[Hole], input: List[UserShotInput]): List[UserShotInput] = filterHole match {
    case Some(h) => input.filter(in => in.hole == h)
    case None    => input
  }

  private[this] def aggregateResults(input: List[UserShotInput], h: Handicap, shotCount: Int): GolfResult =
    GolfResult(
      input.head.gameId,
      findScore(shotCount, input.head.par),
      combineAll(input.map(_.strokesGained)),
      getStrokesGainedOffTheTee(input, input.head.par),
      getStrokesGainedApproachTheGreen(input, input.head.par),
      getStrokesGainedAroundTheGreen(input),
      getStrokesGainedPutting(input),
      StablefordPoints(input.head.par, h, input.head.strokeIndex, shotCount)
    )

  def calculate[F[_]](
    dbOp: GetStat[F]
  )(h: Handicap, input: List[UserShotInput], hole: Option[Hole])(implicit F: Monad[F]): F[StrokesGainded] =
    for {
      inp <- F.point(determineId(hole, input.filter(_.location.locationId <= 6)))
      met <- getMetrics(dbOp)(inp)
      stg <- F.map(F.point(met))(getAllStrokesGained)
    } yield StrokesGainded(aggregateResults(stg, h, countShots(input)), stg)
}
