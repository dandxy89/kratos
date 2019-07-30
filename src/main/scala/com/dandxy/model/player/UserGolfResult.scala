package com.dandxy.model.player

import com.dandxy.model.golf.Score
import com.dandxy.model.player.GolfInput.UserGolfInput

final case class UserGolfResult(
  score: Score,
  strokesGained: Double,
  strokesGainedOffTheTee: Double,
  strokesGainedApproach: Double,
  strokesGainedAround: Double,
  strokesGainedPutting: Double,
  userDate: List[UserGolfInput]
)
