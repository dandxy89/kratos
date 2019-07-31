package com.dandxy.model.golf

sealed trait DistanceMeasurement {
  def toYards(input: Double): Double
  def toFeet(input: Double): Double
  def toMeters(input: Double): Double
}

object DistanceMeasurement {

  case object Yards extends DistanceMeasurement {
    def toYards(input: Double): Double  = input
    def toFeet(input: Double): Double   = input * 3
    def toMeters(input: Double): Double = input * 0.9144
  }

  case object Meters extends DistanceMeasurement {
    def toYards(input: Double): Double  = input * 1.0936133333333
    def toFeet(input: Double): Double   = input * 3.28084
    def toMeters(input: Double): Double = input
  }

  case object Feet extends DistanceMeasurement {
    def toYards(input: Double): Double  = input * 0.33333334399998987285
    def toFeet(input: Double): Double   = input
    def toMeters(input: Double): Double = input * 0.3048
  }
}
