package com.dandxy.golf.input

import doobie.util.Meta
import io.circe.syntax._
import io.circe.{ Decoder, Encoder }

final case class Distance(value: Double) extends AnyVal

object Distance {
  implicit val meta: Meta[Distance]  = Meta[Int].timap(v => Distance(v.toDouble))(v => v.value.toInt)
  implicit val en: Encoder[Distance] = Encoder.instance(_.value.asJson)
  implicit val de: Decoder[Distance] = Decoder.instance(_.as[Double].map(Distance(_)))
}
