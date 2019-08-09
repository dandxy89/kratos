package com.dandxy.strokes

import cats.Monad
import cats.implicits._
import com.dandxy.model.golf.entity.Location.{ OnTheGreen, TeeBox }
import com.dandxy.model.golf.entity.Par.ParThree
import com.dandxy.model.golf.entity.Score
import com.dandxy.model.golf.entity.{ Location, Par }
import com.dandxy.model.golf.input.GolfInput.UserShotInput
import com.dandxy.model.golf.input.{ Distance, Handicap, HoleResult, Strokes }
import com.dandxy.model.golf.pga.Statistic.PGAStatistic
import com.dandxy.util.Helpers.{ combineAll, roundAt3 }
import StablefordCalculator.{ calculate => StablefordPoints }

import scala.annotation.tailrec
import scala.language.higherKinds

object StrokesGainedCalculator {

  def getMetrics[F[_]: Monad](dbOp: Location => Distance => F[PGAStatistic])(input: List[UserShotInput]): F[List[UserShotInput]] =
    input.map { v =>
      dbOp(v.location)(v.distance).map(s => v.copy(strokesGained = Option(Strokes(s.strokes))))
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

  private[this] def aggregateResults(input: List[UserShotInput], h: Handicap, shotCount: Int): HoleResult =
    HoleResult(
      score = Score.findScore(shotCount, input.head.par),
      strokesGained = combineAll(input.map(_.strokesGained)),
      strokesGainedOffTheTee = getStrokesGainedOffTheTee(input, input.head.par),
      strokesGainedApproach = getStrokesGainedApproachTheGreen(input, input.head.par),
      strokesGainedAround = getStrokesGainedAroundTheGreen(input),
      strokesGainedPutting = getStrokesGainedPutting(input),
      stablefordPoints = StablefordPoints(input.head.par, h, input.head.strokeIndex, shotCount),
      userDate = input
    )

  type GetStatistic[F[_]] = Location => Distance => F[PGAStatistic]

  def calculate[F[_]](dbOp: GetStatistic[F])(h: Handicap, input: List[UserShotInput])(implicit F: Monad[F]): F[HoleResult] =
    for {
      met <- getMetrics(dbOp)(input.filter(_.location.locationId <= 6))
      stg <- F.map(F.point(met))(getAllStrokesGained)
    } yield aggregateResults(stg, h, countShots(input))
}
