package com.dandxy.golf.pga

import com.dandxy.golf.input.Distance
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.Encoder
import io.circe.Decoder

sealed trait Statistic

object Statistic {
  final case class PGAStatistic(distance: Distance, strokes: Double) extends Statistic

  object PGAStatistic {
    implicit val e: Encoder[PGAStatistic] = deriveEncoder
    implicit val d: Decoder[PGAStatistic] = deriveDecoder
  }
}

sealed trait Probabilities extends Statistic {
  def distance: Distance
}

object Probabilities {

  final case class PGAPuttingProbability(distance: Distance, onePutt: Double, twoPutt: Double, threePutt: Double, expectedPutt: Double)
      extends Probabilities

  // Fairway // Rough
  final case class UpAndDownPercentage(distance: Distance, fromFairway: Int, fromRough: Int) extends Probabilities //

  // Fairway // Rough
  final case class StrokesToGoPercentage(distance: Distance, fromFairway: Int, fromRough: Int) extends Probabilities

  // Fairway // Rough // Sand
  final case class GreenHitPercentage(distance: Distance, fromFairway: Int, fromRough: Int, fromSand: Int) extends Probabilities

}
