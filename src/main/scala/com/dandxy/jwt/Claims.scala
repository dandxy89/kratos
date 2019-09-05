package com.dandxy.jwt

import cats.implicits._
import com.dandxy.model.player.PlayerId

import scala.util.control.NonFatal

final case class Claims(playerId: String) {
  def asPlayerId: Either[String, PlayerId] =
    Either
      .catchNonFatal(playerId.toInt)
      .map(PlayerId(_))
      .leftMap {
        case NonFatal(e) =>
          e.getMessage
      }
}
