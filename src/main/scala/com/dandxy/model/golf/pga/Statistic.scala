package com.dandxy.model.golf.pga

import com.dandxy.model.golf.input.{Distance, Metric}

sealed trait Statistic

object Statistic {

  final case class PGAStatistic(distance: Distance, strokes: Double) extends Statistic

  final case class StrokesGained(shot: Int, metric: Metric, value: Double) extends Statistic

}
