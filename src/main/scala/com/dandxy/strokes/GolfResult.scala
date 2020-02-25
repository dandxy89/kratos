package com.dandxy.strokes

import cats.implicits._
import cats.kernel.Semigroup
import com.dandxy.golf.entity.Score
import com.dandxy.golf.entity.Score.Aggregate
import com.dandxy.golf.input.{ Points, Strokes }
import com.dandxy.model.user.Identifier.GameId
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class GolfResult(
  id: GameId,
  score: Score,
  strokesGained: Option[Strokes],
  strokesGainedOffTheTee: Option[Strokes],
  strokesGainedApproach: Option[Strokes],
  strokesGainedAround: Option[Strokes],
  strokesGainedPutting: Option[Strokes],
  stablefordPoints: Points
)

object GolfResult {

  def empty(id: GameId): GolfResult =
    GolfResult(id, Aggregate(0), None, None, None, None, None, Points(0))

  // Instances
  implicit val en: Encoder[GolfResult] = deriveEncoder
  implicit val de: Decoder[GolfResult] = deriveDecoder

  implicit val sg: Semigroup[GolfResult] = (x: GolfResult, y: GolfResult) =>
    GolfResult(
      x.id,
      x.score |+| y.score,
      x.strokesGained |+| y.strokesGained,
      x.strokesGainedOffTheTee |+| y.strokesGainedOffTheTee,
      x.strokesGainedApproach |+| y.strokesGainedApproach,
      x.strokesGainedAround |+| y.strokesGainedAround,
      x.strokesGainedPutting |+| y.strokesGainedPutting,
      x.stablefordPoints |+| y.stablefordPoints
    )
}
