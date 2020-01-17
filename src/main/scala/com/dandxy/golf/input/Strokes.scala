package com.dandxy.golf.input

import cats.kernel.Monoid
import io.circe.syntax._
import io.circe.{ Decoder, Encoder }

final case class Strokes(value: Double) extends AnyVal

object Strokes {

  implicit val monoid: Monoid[Strokes] = new Monoid[Strokes] {
    override def empty: Strokes                           = Strokes(0.0)
    override def combine(x: Strokes, y: Strokes): Strokes = Strokes(x.value + y.value)
  }

  implicit val en: Encoder[Strokes] = Encoder.instance(_.value.asJson)
  implicit val de: Decoder[Strokes] = Decoder.instance(_.as[Double].map(Strokes(_)))
}
