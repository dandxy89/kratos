//package com.dandxy.strokes
//
//import cats.Eq
//import cats.effect.IO
//import cats.implicits._
//import com.dandxy.model.error.DomainError
//import com.dandxy.model.error.DomainError.{ InvalidDistance, StatisticNotKnown }
//import com.dandxy.model.golf.DistanceMetric.{ Meters, Yards }
//import com.dandxy.model.golf.Location.OnTheGreen
//import com.dandxy.model.golf.{ Distance, DistanceMetric, Location, PGAStatistic }
//import com.dandxy.model.player.{ UserGolfInput, UserInput }
//
//object StrokesGainedPutting {
//
//  private val feetToYards: Double  = 3.0
//  private val metersToFeet: Double = 3.28084
//
//  def findPuttingStrokes(input: List[UserGolfInput]): List[UserGolfInput] =
//    input.filter { v =>
//      Eq.eqv[Location](v.location, OnTheGreen)
//    }
//
//  def conversion(distance: Distance, metric: DistanceMetric): Distance =
//    metric match {
//      case Yards  => Distance(feetToYards * distance.value)
//      case Meters => Distance(metersToFeet * distance.value)
//      case _      => distance
//    }
//
//  def calculate(nPutts: Int, distance: Distance, statistic: IO[PGAStatistic]): IO[Either[DomainError, Double]] =
//    distance match {
//      case _ if distance.value < 0   => IO.pure(InvalidDistance(distance).asLeft)
//      case _ if distance.value > 100 => IO.pure(StatisticNotKnown(distance).asLeft)
//      case _                         => statistic.map { case PGAStatistic(_, s) => (nPutts - s).asRight }
//    }
//
//  def getStrokesGained(dbCall: Distance => IO[PGAStatistic])(input: UserInput): IO[Either[DomainError, Double]] = {
//    val puttingStrokesOnly: List[UserGolfInput] = findPuttingStrokes(input.golfInput)
//    val distanceToHole: Distance                = Distance(puttingStrokesOnly.map(_.distance.value).max)
//    val distanceAsFeet: Distance                = conversion(distanceToHole, input.metric)
//
//    calculate(puttingStrokesOnly.size, distanceAsFeet, dbCall(distanceAsFeet))
//  }
//}
