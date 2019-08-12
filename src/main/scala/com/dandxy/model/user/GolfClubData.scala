package com.dandxy.model.user

import com.dandxy.model.golf.entity.{GolfClub, Manufacturer}
import com.dandxy.model.golf.input.{Distance, DistanceMeasurement, ShotHeight, ShotShape}
import com.dandxy.model.player.PlayerId

final case class GolfClubData(
  playerId: PlayerId,
  club: GolfClub,
  typicalShape: Option[ShotShape],
  typicalHeight: Option[ShotHeight],
  manufacturer: Option[Manufacturer],
  typicalDistance: Distance,
  distanceType: DistanceMeasurement
)
