package com.dandxy.model.golf.input

import com.dandxy.model.golf.entity._

sealed trait GolfInput

object GolfInput {

  final case class HoleInput(
    distance: Distance,
    shot: Int,
    location: Location,
    orientation: Option[Orientation],
    club: GolfClub,
    shotShape: Option[ShotShape],
    shotHeight: Option[ShotHeight],
    strokeIndex: Int
  ) extends GolfInput

  final case class UserGameInput(
    userId: String,
    puttingMetric: DistanceMeasurement,
    hole: Hole,
    par: Par,
    golfInput: List[HoleInput],
    handicap: Int,
    ballUsed: Option[String],
    greenSpeed: Option[Double],
    temperature: Option[Temperature],
    windSpeed: Option[WindSpeed]
  ) extends GolfInput
}
