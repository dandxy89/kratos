package com.dandxy.jwt

import java.time.{ LocalDateTime, ZoneOffset }

import com.dandxy.model.player.PlayerId
import io.circe.Json
import io.circe.syntax._
import pdi.jwt.{ Jwt, JwtAlgorithm, JwtClaim }

object GenerateToken {

  def prepareToken(offsetHours: Long, key: String, algorithm: JwtAlgorithm = JwtAlgorithm.HS256)(playerId: PlayerId): String = {
    val now = LocalDateTime.now()

    val claim = JwtClaim(
      Json.obj("playerId" -> playerId.asJson).noSpaces,
      None,
      Some("golfer"),
      None,
      Some(now.plusHours(offsetHours).toEpochSecond(ZoneOffset.UTC)),
      None,
      Some(now.toEpochSecond(ZoneOffset.UTC)),
      None
    )

    Jwt.encode(claim, key, algorithm)
  }
}
