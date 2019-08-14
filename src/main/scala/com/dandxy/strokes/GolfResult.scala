package com.dandxy.strokes

import com.dandxy.golf.entity.Score
import com.dandxy.golf.input.{Points, Strokes}
import com.dandxy.model.user.Identifier.GameId

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
