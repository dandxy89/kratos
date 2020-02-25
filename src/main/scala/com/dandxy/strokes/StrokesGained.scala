package com.dandxy.strokes

import com.dandxy.golf.input.GolfInput.UserShotInput

final case class StrokesGained(result: GolfResult, shots: List[UserShotInput])
