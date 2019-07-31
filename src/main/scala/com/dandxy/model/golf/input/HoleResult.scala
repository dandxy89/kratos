package com.dandxy.model.golf.input

import com.dandxy.model.golf.entity.Score
import com.dandxy.model.golf.input.GolfInput.HoleInput

final case class HoleResult(
  score: Score,
  strokesGained: Double,
  strokesGainedOffTheTee: Double,
  strokesGainedApproach: Double,
  strokesGainedAround: Double,
  strokesGainedPutting: Double,
  userDate: List[HoleInput]
)
