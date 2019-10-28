package com.dandxy.model.user

import io.circe.syntax._
import io.circe.{Decoder, Encoder}

final case class ShotSerialId(id: Int)

object ShotSerialId {
  implicit val en: Encoder[ShotSerialId] = Encoder.instance(_.id.asJson)
  implicit val de: Decoder[ShotSerialId] = Decoder.instance(_.as[Int].map(ShotSerialId(_)))
}
