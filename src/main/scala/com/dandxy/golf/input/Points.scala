package com.dandxy.golf.input

import cats.kernel.Semigroup
import io.circe.syntax._
import io.circe.{ Decoder, Encoder }

final case class Points(value: Int)

object Points {
  implicit val en: Encoder[Points]   = Encoder.instance(_.value.asJson)
  implicit val de: Decoder[Points]   = Decoder.instance(_.as[Int].map(Points(_)))
  implicit val sg: Semigroup[Points] = (x: Points, y: Points) => Points(x.value + y.value)
}
