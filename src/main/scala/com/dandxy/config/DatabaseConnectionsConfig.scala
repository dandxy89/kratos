package com.dandxy.config

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class DatabaseConnectionsConfig(poolSize: Int) extends AnyVal

object DatabaseConnectionsConfig {
  implicit val decoder: Decoder[DatabaseConnectionsConfig] = deriveDecoder
}
