package com.dandxy.jwt

import java.time.{LocalDateTime, ZoneOffset}

import io.circe.syntax._
import com.dandxy.model.player.PlayerId
import pdi.jwt.JwtClaim

object GenerateToken {

  def prepareToken(issuer: String)(playerId: PlayerId): JwtClaim = {
    val now = LocalDateTime.now()

    JwtClaim(
      playerId.asJson.spaces2,
      Some(issuer),
      Some("golfer"),
      None,
      Some(now.plusHours(2).toEpochSecond(ZoneOffset.UTC)),
      None,
      Some(now.toEpochSecond(ZoneOffset.UTC)),
      None
    )
  }
}
