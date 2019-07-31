package com.dandxy.model.player

import com.dandxy.model.golf.entity.{GolfClub, Manufacturer}
import com.dandxy.model.golf.input.{Distance, DistanceMeasurement, ShotHeight, ShotShape}

sealed trait PlayerDetail {
  def playerId: PlayerId
}

object PlayerDetail {

  final case class UserProfile(playerId: PlayerId, firstName: String, lastName: String, handicap: Double, password: String)
      extends PlayerDetail

  final case class GolfClubData(
    playerId: PlayerId,
    club: GolfClub,
    typicalShape: Option[ShotShape],
    typicalHeight: Option[ShotHeight],
    manufacturer: Option[Manufacturer],
    typicalDistance: Distance,
    distanceType: DistanceMeasurement
  ) extends PlayerDetail

  final case class UserClubData(playerId: PlayerId, clubs: List[GolfClubData])

}
