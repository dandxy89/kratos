package com.dandxy.model.golf.input

import java.time.LocalDate

import com.dandxy.model.golf.entity._
import com.dandxy.model.player.PlayerId

sealed trait GolfInput

object GolfInput {

  final case class ShotInput(
    distance: Distance,
    shot: Int,
    location: Location,
    orientation: Option[Orientation],
    club: GolfClub,
    shotShape: Option[ShotShape],
    shotHeight: Option[ShotHeight],
    strokeIndex: Int
  ) extends GolfInput

  final case class UserHoleInput(
    playerId: PlayerId,
    puttingMetric: DistanceMeasurement,
    hole: Hole,
    par: Par,
    golfInput: List[ShotInput],
    handicap: Int,
    ballUsed: Option[String],
    greenSpeed: Option[Double],
    temperature: Option[Temperature],
    windSpeed: Option[WindSpeed]
  ) extends GolfInput

  final case class UserCourseInput(courseName: String, holeInput: List[UserHoleInput], gameStartTime: LocalDate) extends GolfInput

}
