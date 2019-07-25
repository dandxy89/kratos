package com.dandxy.model.golf

sealed trait Statistic

object Statistic {

  final case class PGAStatistic(distance: Distance, strokes: Double) extends Statistic

  final case class StrokesGained(shot: Int, metric: Metric, value: Double) extends Statistic

}
