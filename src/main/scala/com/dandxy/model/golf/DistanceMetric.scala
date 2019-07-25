package com.dandxy.model.golf

sealed trait DistanceMetric

object DistanceMetric {

  case object Yards extends DistanceMetric

  case object Meters extends DistanceMetric

  case object Feet extends DistanceMetric

}
