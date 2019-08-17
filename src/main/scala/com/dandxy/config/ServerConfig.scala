package com.dandxy.config

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class ServerConfig(host: String, port: Int)

object ServerConfig {
  implicit val decoder: Decoder[ServerConfig] = deriveDecoder
}
