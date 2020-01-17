package com.dandxy.model.player

import io.circe.syntax._
import io.circe.{ Decoder, Encoder }

final case class PlayerId(id: Int)

object PlayerId {
  implicit val e: Encoder[PlayerId] = Encoder.instance(_.id.asJson)
  implicit val d: Decoder[PlayerId] = Decoder.instance(_.as[Int].map(PlayerId(_)))
}
