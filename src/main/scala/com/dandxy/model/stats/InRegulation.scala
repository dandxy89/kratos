package com.dandxy.model.stats

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class InRegulation(count: Int, totalPlayed: Int)

object InRegulation {
  implicit val en: Encoder[InRegulation] = deriveEncoder
  implicit val de: Decoder[InRegulation] = deriveDecoder
}
