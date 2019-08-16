package com.dandxy

import com.dandxy.auth.Salt
import io.circe.Decoder
import io.circe.generic.semiauto._

package object config {
  implicit val saltDecoder: Decoder[Salt]                      = deriveDecoder
  implicit val authDecoder: Decoder[AuthSalt]                  = deriveDecoder
  implicit val connDecoder: Decoder[DatabaseConnectionsConfig] = deriveDecoder
  implicit val doobDecoder: Decoder[DatabaseConfig]            = deriveDecoder
  implicit val servDecoder: Decoder[ServerConfig]              = deriveDecoder
  implicit val applDecoder: Decoder[ApplicationConfig]         = deriveDecoder
}
