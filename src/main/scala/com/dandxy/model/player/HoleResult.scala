package com.dandxy.model.player

import com.dandxy.model.golf.Score
import com.dandxy.model.player.GolfInput.HoleInput

final case class HoleResult(
  score: Score,
  strokesGained: Double,
  strokesGainedOffTheTee: Double,
  strokesGainedApproach: Double,
  strokesGainedAround: Double,
  strokesGainedPutting: Double,
  userDate: List[HoleInput]
)

// TODO
// Fairways in regulation
// Greens in regulations
// No. Bunkers hit
// No. Putts
// No. Drovers
