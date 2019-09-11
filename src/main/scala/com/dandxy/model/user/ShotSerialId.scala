package com.dandxy.model.user

import doobie.util.Meta
import io.circe.{ Decoder, Encoder }
import io.circe.syntax._

final case class ShotSerialId(id: Int)

object ShotSerialId {
  // Instances
  implicit val meta: Meta[ShotSerialId]  = Meta[Int].imap(ShotSerialId(_))(_.id)
  implicit val en: Encoder[ShotSerialId] = Encoder.instance(_.id.asJson)
  implicit val de: Decoder[ShotSerialId] = Decoder.instance(_.as[Int].map(ShotSerialId(_)))
}
