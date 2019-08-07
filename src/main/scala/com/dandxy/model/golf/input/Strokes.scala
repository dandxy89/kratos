package com.dandxy.model.golf.input

import cats.kernel.Monoid

final case class Strokes(value: Double) extends AnyVal

object Strokes {

  implicit val monoid: Monoid[Strokes] = new Monoid[Strokes] {
    override def empty: Strokes                           = Strokes(0.0)
    override def combine(x: Strokes, y: Strokes): Strokes = Strokes(x.value + y.value)
  }
}
