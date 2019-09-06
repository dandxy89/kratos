package com.dandxy.jwt

import java.time.{ LocalDateTime, ZoneOffset }

import com.dandxy.model.player.PlayerId
import io.circe.syntax._
import pdi.jwt.{ Jwt, JwtAlgorithm, JwtClaim }

object GenerateToken {

  def prepareToken(key: String, issuer: String, algorithm: JwtAlgorithm = JwtAlgorithm.HS256)(playerId: PlayerId): String = {
    val now = LocalDateTime.now()

    val claim = JwtClaim(
      playerId.asJson.spaces2,
      Some(issuer),
      Some("golfer"),
      None,
      Some(now.plusHours(2).toEpochSecond(ZoneOffset.UTC)),
      None,
      Some(now.toEpochSecond(ZoneOffset.UTC)),
      None
    )

    Jwt.encode(claim, key, algorithm)
  }
}
