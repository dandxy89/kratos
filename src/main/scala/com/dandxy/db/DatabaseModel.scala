package com.dandxy.db

import com.dandxy.model.golf.input.Distance

trait DatabaseModel {
  def distance: Distance
}

object DatabaseModel {

  final case class Statistic(distance: Distance, strokes: Double) extends DatabaseModel

  final case class PGAPuttingProbability(
    distance: Distance,
    onePutt: Double,
    twoPutt: Double,
    threePutt: Double,
    expectedPutt: Double
  ) extends DatabaseModel

  final case class UpAndDownPercentage(distance: Distance, fromFairway: Int, fromRough: Int) extends DatabaseModel

  final case class StrokesToGoPercentage(distance: Distance, fromFairway: Int, fromRough: Int) extends DatabaseModel

  final case class GreenHitPercentage(distance: Distance, fromFairway: Int, fromRough: Int, fromSand: Int) extends DatabaseModel

}
