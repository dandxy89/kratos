package com.dandxy.golf.input

import java.sql.Timestamp

import com.dandxy.golf.entity._
import com.dandxy.model.player.PlayerId
import com.dandxy.model.user.Identifier.{ GameId, Hole }
import com.dandxy.model.user.ShotSerialId
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import com.dandxy.util.Codecs.TimestampFormat

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

  // Instances
  implicit val en: Encoder[UserGameInput] = deriveEncoder

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
