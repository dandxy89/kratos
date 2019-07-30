package com.dandxy.strokes

import cats.Monad
import cats.implicits._
import com.dandxy.model.golf.Location.OnTheGreen
import com.dandxy.model.golf.Par.ParThree
import com.dandxy.model.golf.Statistic.PGAStatistic
import com.dandxy.model.golf.{Distance, Location, Par}
import com.dandxy.model.player.GolfInput.{InputAndMetric, UserGolfInput}

import scala.annotation.tailrec
import scala.language.higherKinds

object StokesGainedCalculator {

  def getMetrics[F[_]: Monad](dbOp: Location => Distance => F[PGAStatistic])(input: List[UserGolfInput]): F[List[InputAndMetric]] =
    input.map { v =>
      dbOp(v.location)(v.distance).map(s => InputAndMetric(v, s, 0))
    }.sequence

  private def roundAt3(n: Double): Double = {
    val s = math pow (10, 3)
    (math round n * s) / s
  }

  @tailrec
  private def tailRecResults(in: List[InputAndMetric], out: List[InputAndMetric]): List[InputAndMetric] = in match {
    case Nil => out
    case oneResult :: Nil =>
      oneResult.copy(result = roundAt3((oneResult.statistic.strokes - 0) - 1)) :: out
    case f :: s :: t =>
      tailRecResults(s :: t, InputAndMetric(f.data, f.statistic, roundAt3((f.statistic.strokes - s.statistic.strokes) - 1)) :: out)
  }

  def getAllStrokesGained(in: List[InputAndMetric]): List[InputAndMetric] = tailRecResults(in, Nil).reverse

  // Strokes Gained: Putting
  def getStrokesGainedPutting(in: List[InputAndMetric]): Double =
    in.filter(_.data.location == OnTheGreen).map(_.result).sum

  // Strokes Gained: Off-the-tee
  def getStrokesGainedTee(in: List[InputAndMetric], par: Par): Double =
    if (par == ParThree) 0.0
    else {
      in match {
        case head :: _ => head.result
        case _         => 0.0
      }
    }
}
