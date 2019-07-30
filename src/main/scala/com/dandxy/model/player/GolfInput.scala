package com.dandxy.model.player

import com.dandxy.model.golf.{ Distance, DistanceMetric, GolfClub, Hole, Location, Orientation, Par }

trait GolfInput

object GolfInput {

  final case class UserGolfInput(
    distance: Distance,
    shot: Int,
    location: Location,
    orientation: Option[Orientation],
    club: GolfClub
  ) extends GolfInput

  final case class UserInput (
    userId: String,
    puttingMetric: DistanceMetric,
    hole: Hole,
    par: Par,
    golfInput: List[UserGolfInput]
  ) extends GolfInput

  final case class UserProfile(playerId: PlayerId, firstName: String, lastName: String, handicap: Double) extends GolfInput

}
