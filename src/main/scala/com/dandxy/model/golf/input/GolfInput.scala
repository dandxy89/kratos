package com.dandxy.model.golf.input

import java.sql.Timestamp

import com.dandxy.model.golf.entity._
import com.dandxy.model.player.PlayerId
import com.dandxy.model.user.Identifier.{ GameId, Hole }
import com.dandxy.model.user.ShotSerialId

sealed trait GolfInput

object GolfInput {

  final case class UserGameInput(
    playerId: PlayerId,
    gameStartTime: Timestamp,
    courseName: String,
    handicap: Handicap,
    ballUsed: Option[String],
    greenSpeed: Option[Double],
    temperature: Option[Temperature],
    windSpeed: Option[WindSpeed],
    gameId: Option[GameId]
  ) extends GolfInput

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
    shotHeight: Option[ShotHeight],
    shotId: Option[ShotSerialId]
  ) extends GolfInput
}
