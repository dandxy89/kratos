package com.dandxy.model.user

import com.dandxy.golf.entity.{GolfClub, Manufacturer}
import com.dandxy.golf.input.{Distance, DistanceMeasurement, ShotHeight, ShotShape}
import com.dandxy.model.player.PlayerId
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

final case class GolfClubData(
  playerId: PlayerId,
  club: GolfClub,
  typicalShape: Option[ShotShape],
  typicalHeight: Option[ShotHeight],
  manufacturer: Option[Manufacturer],
  typicalDistance: Distance,
  distanceType: DistanceMeasurement
)

object GolfClubData {
  // Instances
  implicit val en: Encoder[GolfClubData] = deriveEncoder
}
