package com.dandxy.model.golf.input

import java.sql.Timestamp

import com.dandxy.model.golf.entity._
import com.dandxy.model.user.{ GameId, PlayerId }

sealed trait GolfInput

object GolfInput {

  final case class UserGameInput(
    playerId: PlayerId,
    distanceMeasurement: DistanceMeasurement,
    gameStartTime: Timestamp,
    courseName: String,
    handicap: Double,
    ballUsed: Option[String],
    greenSpeed: Option[Double],
    temperature: Option[Temperature],
    windSpeed: Option[WindSpeed]
  ) extends GolfInput // RETURNS A GameId

  final case class UserShotInput(
    gameId: GameId,
    hole: Hole,
    shot: Int,
    par: Par,
    distance: Distance,
    location: Location,
    club: GolfClub,
    strokesGained: Option[Strokes],
    strokeIndex: Int,
    orientation: Option[Orientation],
    shotShape: Option[ShotShape],
    shotHeight: Option[ShotHeight]
  ) extends GolfInput // RETURNS ShotSerialId
}
