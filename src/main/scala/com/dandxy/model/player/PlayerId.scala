package com.dandxy.model.player

import doobie.util.Meta
import io.circe.generic.semiauto._
import io.circe.{ Decoder, Encoder }
import io.circe.syntax._

final case class PlayerId(id: Int)

object PlayerId {

  // Instances
  implicit val meta: Meta[PlayerId] = Meta[Int].imap(PlayerId(_))(_.id)
  implicit val e: Encoder[PlayerId] = Encoder.instance(_.id.asJson)
  implicit val d: Decoder[PlayerId] = deriveDecoder

}
