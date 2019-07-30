package com.dandxy.model.golf

sealed trait DistanceMetric {
  def toYards(input: Double): Double
}

object DistanceMetric {

  case object Yards extends DistanceMetric {
    def toYards(input: Double): Double = 3.0 * input
  }

  case object Meters extends DistanceMetric {
    def toYards(input: Double): Double = 3.28084 * input
  }

  case object Feet extends DistanceMetric {
    def toYards(input: Double): Double = input
  }
}
