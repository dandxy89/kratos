package com.dandxy.auth

import com.dandxy.model.user.{ Password, PlayerId }

final case class PlayerHash(playerId: PlayerId, password: Password)
