package com.dandxy.strokes

import cats.effect.IO
import cats.implicits._
import com.dandxy.model.golf.DistanceMetric.{Meters, Yards}
import com.dandxy.model.golf.Location.OnTheGreen
import com.dandxy.model.golf.Statistic.{PGAStatistic, StrokesGained}
import com.dandxy.model.golf.{Distance, DistanceMetric, Location, PGAStatistics}
import com.dandxy.model.player.GolfInput.{UserGolfGolfInput, UserGolfInput}

object StrokesGained {

  final case class Result(strokesGained: List[StrokesGained], previous: Option[PGAStatistic])

  private val feetToYards: Double  = 3.0
  private val metersToFeet: Double = 3.28084

  def conversion(distance: Distance, metric: DistanceMetric): Distance =
    metric match {
      case Yards  => Distance(feetToYards * distance.value)
      case Meters => Distance(metersToFeet * distance.value)
      case _      => distance
    }

  def getMetrics(lookup: Distance => Location => IO[PGAStatistic],
                 in: List[UserGolfGolfInput], metric: DistanceMetric): List[IO[PGAStatistic]] =
    in.map { i =>
      if (i.location == OnTheGreen) lookup(conversion(i.distance, metric))(i.location)
      else lookup(i.distance)(i.location)
    }

  def calculate(
    lookup: Distance => Location => IO[PGAStatistic]
  )(puttingMetric: DistanceMetric, strokes: UserGolfInput): List[StrokesGained] = {
    val a: List[IO[PGAStatistic]] = getMetrics(lookup, strokes.golfInput, strokes.puttingMetric)


    val aa: IO[List[Result]] = strokes
      .golfInput
      .zip(a)
        .foldLeftM[IO, List[Result]](Nil){ case (a1, a2) => a1 match {
        case Nil => List(Result(, a2.))
        case ab =>
      }
    }



    ???
  }

}
