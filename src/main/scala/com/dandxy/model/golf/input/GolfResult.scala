package com.dandxy.model.golf.input

import com.dandxy.model.golf.entity.Score
import com.dandxy.model.golf.input.GolfInput.UserShotInput
import com.dandxy.model.user.Identifier

final case class GolfResult(
  id: Identifier,
  score: Score,
  strokesGained: Option[Strokes],
  strokesGainedOffTheTee: Option[Strokes],
  strokesGainedApproach: Option[Strokes],
  strokesGainedAround: Option[Strokes],
  strokesGainedPutting: Option[Strokes],
  stablefordPoints: Points,
  userDate: List[UserShotInput]
)
