package com.dandxy.strokes

import cats.Eq
import cats.effect.IO
import cats.implicits._
import com.dandxy.model.error.DomainError
import com.dandxy.model.error.DomainError.{ InvalidDistance, StatisticNotKnown }
import com.dandxy.model.golf.Location.OnTheGreen
import com.dandxy.model.golf.Statistic.PGAStatistic
import com.dandxy.model.golf.{ Distance, DistanceMetric, Location }
import com.dandxy.model.player.GolfInput.{ UserGolfInput, UserInput }

object StrokesGainedPutting {

  def findPuttingStrokes(input: List[UserGolfInput]): List[UserGolfInput] =
    input.filter(v => Eq.eqv[Location](v.location, OnTheGreen))

  def conversion(distance: Distance, metric: DistanceMetric): Distance =
    Distance(metric.toYards(distance.value))

  def calculate(nPutts: Int, distance: Distance, statistic: IO[PGAStatistic]): IO[Either[DomainError, Double]] =
    distance match {
      case _ if distance.value < 0   => IO.pure(InvalidDistance(distance).asLeft)
      case _ if distance.value > 100 => IO.pure(StatisticNotKnown(distance).asLeft)
      case _                         => statistic.map { case PGAStatistic(_, s) => (nPutts - s).asRight }
    }

  def getStrokesGained(dbCall: Distance => IO[PGAStatistic])(input: UserInput): IO[Either[DomainError, Double]] = {
    val puttingStrokesOnly = findPuttingStrokes(input.golfInput)
    val distanceToHole     = Distance(puttingStrokesOnly.map(_.distance.value).max)
    val distanceAsFeet     = conversion(distanceToHole, input.puttingMetric)

    calculate(puttingStrokesOnly.size, distanceAsFeet, dbCall(distanceAsFeet))
  }
}
