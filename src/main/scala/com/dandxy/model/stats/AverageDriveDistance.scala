package com.dandxy.model.stats

import com.dandxy.golf.entity.GolfClub
import com.dandxy.golf.input.Distance
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

final case class AverageDriveDistance(club: GolfClub, distance: Distance)

object AverageDriveDistance {
  implicit val en: Encoder[AverageDriveDistance] = deriveEncoder
  implicit val de: Decoder[AverageDriveDistance] = deriveDecoder
}
