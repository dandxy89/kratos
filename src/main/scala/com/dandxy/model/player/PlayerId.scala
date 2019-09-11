package com.dandxy.model.player

import doobie.util.Meta
import io.circe.syntax._
import io.circe.{ Decoder, Encoder }

final case class PlayerId(id: Int)

object PlayerId {
  // Instances
  implicit val meta: Meta[PlayerId] = Meta[Int].imap(PlayerId(_))(_.id)
  implicit val e: Encoder[PlayerId] = Encoder.instance(_.id.asJson)
  implicit val d: Decoder[PlayerId] = Decoder.instance(_.as[Int].map(PlayerId(_)))
}
