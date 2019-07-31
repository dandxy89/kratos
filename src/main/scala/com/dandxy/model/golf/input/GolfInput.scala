package com.dandxy.model.golf.input

import com.dandxy.model.golf.entity._

sealed trait GolfInput

object GolfInput {

  final case class HoleInput(
    distance: Distance,
    shot: Int,
    location: Location,
    orientation: Option[Orientation],
    club: GolfClub
  ) extends GolfInput

  // Shape
  // Height
  // Stroke index

  final case class UserGameInput(userId: String, puttingMetric: DistanceMeasurement, hole: Hole, par: Par, golfInput: List[HoleInput])
      extends GolfInput

  // Ball used
  // Handicap
  // Green speed
  // Temperature
  // Wind

}
