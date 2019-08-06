package com.dandxy.model.golf.pga

import com.dandxy.model.golf.input.Distance

sealed trait Statistic

object Statistic {

  final case class PGAStatistic(distance: Distance, strokes: Double) extends Statistic

}

sealed trait Probabilities extends Statistic {
  def distance: Distance
}

object Probabilities {

  final case class PGAPuttingProbability( // Putting
    distance: Distance,
    onePutt: Double,
    twoPutt: Double,
    threePutt: Double,
    expectedPutt: Double
  ) extends Probabilities

  // Fairway // Rough
  final case class UpAndDownPercentage(distance: Distance, fromFairway: Int, fromRough: Int) extends Probabilities //

  // Fairway // Rough
  final case class StrokesToGoPercentage(distance: Distance, fromFairway: Int, fromRough: Int) extends Probabilities

  // Fairway // Rough // Sand
  final case class GreenHitPercentage(distance: Distance, fromFairway: Int, fromRough: Int, fromSand: Int) extends Probabilities

}
