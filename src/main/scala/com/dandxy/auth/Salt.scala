package com.dandxy.auth

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final case class Salt(base64: String) extends AnyVal

object Salt {
  implicit val decoder: Decoder[Salt] = deriveDecoder
}
