package com.dandxy.config

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class JWTConfig(issuer: String, key: String)

object JWTConfig {
  implicit val decoder: Decoder[JWTConfig] = deriveDecoder
}
