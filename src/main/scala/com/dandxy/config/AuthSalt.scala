package com.dandxy.config

import com.dandxy.auth.Salt
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class AuthSalt(salt: Option[Salt])

object AuthSalt {
  implicit val decoder: Decoder[AuthSalt] = deriveDecoder
}
