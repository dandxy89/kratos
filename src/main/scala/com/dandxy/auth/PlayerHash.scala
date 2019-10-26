package com.dandxy.auth

import com.dandxy.model.player.PlayerId

final case class PlayerHash(playerId: PlayerId, password: Password)
