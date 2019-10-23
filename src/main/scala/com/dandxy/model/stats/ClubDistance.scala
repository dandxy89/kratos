package com.dandxy.model.stats

import com.dandxy.golf.entity.GolfClub
import com.dandxy.golf.input.Distance
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class ClubDistance(club: GolfClub, distance: Distance)

object ClubDistance {
  implicit val en: Encoder[ClubDistance] = deriveEncoder
  implicit val de: Decoder[ClubDistance] = deriveDecoder
}
