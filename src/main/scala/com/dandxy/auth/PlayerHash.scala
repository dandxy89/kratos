package com.dandxy.auth

import com.dandxy.model.player.PlayerId
import com.dandxy.model.user.Password

final case class PlayerHash(playerId: PlayerId, password: Password)
