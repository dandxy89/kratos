package com.dandxy.strokes

import com.dandxy.golf.entity.Score
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
  // Instances
  implicit val en: Encoder[GolfResult] = deriveEncoder
  implicit val de: Decoder[GolfResult] = deriveDecoder
}
