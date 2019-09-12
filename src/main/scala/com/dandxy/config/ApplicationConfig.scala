package com.dandxy.config

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class ApplicationConfig(jdbc: DatabaseConfig, auth: AuthSalt, server: ServerConfig, jwt: JWTConfig)

object ApplicationConfig {
  implicit val decoder: Decoder[ApplicationConfig] = deriveDecoder
}
