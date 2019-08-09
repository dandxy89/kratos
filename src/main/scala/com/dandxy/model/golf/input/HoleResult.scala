package com.dandxy.model.golf.input

import com.dandxy.model.golf.entity.Score
import com.dandxy.model.golf.input.GolfInput.UserShotInput

final case class HoleResult(
  score: Score,
  strokesGained: Option[Strokes],
  strokesGainedOffTheTee: Option[Strokes],
  strokesGainedApproach: Option[Strokes],
  strokesGainedAround: Option[Strokes],
  strokesGainedPutting: Option[Strokes],
  stablefordPoints: Points,
  userDate: List[UserShotInput]
)
