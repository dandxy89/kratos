package com.dandxy.model.player

import doobie.util.Meta
import io.circe.{ Decoder, Encoder, Json }
import io.circe.generic.semiauto._
import io.circe.syntax._

final case class PlayerId(id: Int)

object PlayerId {

  implicit val meta: Meta[PlayerId] = Meta[Int].imap(PlayerId(_))(_.id)

  implicit val e: Encoder[PlayerId] = Encoder.instance(p => Json.obj(("playerId", p.id.toString.asJson)))
  implicit val d: Decoder[PlayerId] = deriveDecoder

}
