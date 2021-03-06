package com.dandxy.golf.input

import io.circe.syntax._
import io.circe.{ Decoder, Encoder }

sealed trait DistanceMeasurement {
  def toYards(input: Double): Double
  def toFeet(input: Double): Double
  def toMeters(input: Double): Double
  def id: Int
}

object DistanceMeasurement {

  case object Yards extends DistanceMeasurement {
    def toYards(input: Double): Double  = input
    def toFeet(input: Double): Double   = input * 3
    def toMeters(input: Double): Double = input * 0.9144
    val id: Int                         = 1
  }

  case object Meters extends DistanceMeasurement {
    def toYards(input: Double): Double  = input * 1.0936133333333
    def toFeet(input: Double): Double   = input * 3.28084
    def toMeters(input: Double): Double = input
    val id: Int                         = 2
  }

  case object Feet extends DistanceMeasurement {
    def toYards(input: Double): Double  = input * 0.33333334399998987285
    def toFeet(input: Double): Double   = input
    def toMeters(input: Double): Double = input * 0.3048
    val id: Int                         = 3
  }

  def fromId(value: Int): DistanceMeasurement = value match {
    case 1 => Yards
    case 2 => Meters
    case _ => Feet
  }

  implicit val en: Encoder[DistanceMeasurement] = Encoder.instance(_.id.asJson)
  implicit val de: Decoder[DistanceMeasurement] = Decoder.instance(_.as[Int].map(fromId))
}
