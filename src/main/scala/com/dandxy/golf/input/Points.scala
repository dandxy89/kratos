package com.dandxy.golf.input

import doobie.util.Meta
import io.circe.syntax._
import io.circe.{ Decoder, Encoder }

final case class Points(value: Int)

object Points {
  // Instances
  implicit val meta: Meta[Points]  = Meta[Int].imap(Points(_))(_.value)
  implicit val en: Encoder[Points] = Encoder.instance(_.value.asJson)
  implicit val de: Decoder[Points] = Decoder.instance(_.as[Int].map(Points(_)))
}
