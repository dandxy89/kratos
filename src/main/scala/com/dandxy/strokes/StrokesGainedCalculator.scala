package com.dandxy.strokes

import cats.Monad
import cats.implicits._
import com.dandxy.model.golf.Location.{OnTheGreen, TeeBox}
import com.dandxy.model.golf.Par.ParThree
import com.dandxy.model.golf.Score.findScore
import com.dandxy.model.golf.Statistic.PGAStatistic
import com.dandxy.model.golf.{Distance, Location, Par}
import com.dandxy.model.player.GolfInput.HoleInput
import com.dandxy.model.player.HoleResult

import scala.annotation.tailrec
import scala.language.higherKinds

object StrokesGainedCalculator {

  final case class InputAndMetric(data: HoleInput, statistic: PGAStatistic, result: Double)

  def getMetrics[F[_]: Monad](dbOp: Location => Distance => F[PGAStatistic])(input: List[HoleInput]): F[List[InputAndMetric]] =
    input.map { v =>
      dbOp(v.location)(v.distance).map(s => InputAndMetric(v, s, 0))
    }.sequence

  private def roundAt3(n: Double): Double = {
    val s = math pow (10, 3)
    (math round n * s) / s
  }

  @tailrec
  private def tailRecResults(in: List[InputAndMetric], out: List[InputAndMetric]): List[InputAndMetric] = in match {
    case Nil =>
      out
    case oneResult :: Nil =>
      oneResult.copy(result = roundAt3((oneResult.statistic.strokes - 0) - 1)) :: out
    case f :: s :: t =>
      tailRecResults(s :: t, InputAndMetric(f.data, f.statistic, roundAt3((f.statistic.strokes - s.statistic.strokes) - 1)) :: out)
  }

  def getAllStrokesGained(in: List[InputAndMetric]): List[InputAndMetric] = tailRecResults(in, Nil).reverse

  def getStrokesGainedPutting(in: List[InputAndMetric]): Double =
    in.filter(_.data.location == OnTheGreen).map(_.result).sum

  def getStrokesGainedOffTheTee(in: List[InputAndMetric], par: Par): Double =
    if (par == ParThree) 0.0
    else {
      in match {
        case head :: _ => head.result
        case _         => 0.0
      }
    }

  private def filterApproachShots(in: List[InputAndMetric]): List[InputAndMetric] = in.filter { v =>
    Set(v.data.location).subsetOf(Location.approachLies)
  }

  def getStrokesGainedApproachTheGreen(in: List[InputAndMetric], par: Par): Double = {
    val interim =
      if (par == ParThree) filterApproachShots(in)
      else filterApproachShots(in).filter(_.data.location != TeeBox)

    interim.filter(v => v.data.distance.value > 30).map(_.result).sum
  }

  def getStrokesGainedAroundTheGreen(in: List[InputAndMetric], par: Par): Double =
    filterApproachShots(in).filter(v => v.data.distance.value <= 30).map(_.result).sum

  def calculate[F[_]: Monad](dbOp: Location => Distance => F[PGAStatistic])(input: List[HoleInput], par: Par): F[HoleResult] =
    getMetrics(dbOp)(input).map(getAllStrokesGained).map { userAndMetrics =>
      val run = getAllStrokesGained(userAndMetrics)
      HoleResult(
        score = findScore(input.size, par),
        strokesGained = run.map(_.result).sum,
        strokesGainedOffTheTee = getStrokesGainedOffTheTee(run, par),
        strokesGainedApproach = getStrokesGainedApproachTheGreen(run, par),
        strokesGainedAround = getStrokesGainedAroundTheGreen(run, par),
        strokesGainedPutting = getStrokesGainedPutting(run),
        userDate = input
      )
    }
}
