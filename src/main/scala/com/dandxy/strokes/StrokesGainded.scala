package com.dandxy.strokes
import com.dandxy.golf.input.GolfInput.UserShotInput

final case class StrokesGainded(result: GolfResult, shots: List[UserShotInput])
