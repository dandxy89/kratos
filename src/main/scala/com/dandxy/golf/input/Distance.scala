package com.dandxy.golf.input

import doobie.util.Meta

final case class Distance(value: Double) extends AnyVal

object Distance {
  implicit val meta: Meta[Distance] = Meta[Int].timap(v => Distance(v.toDouble))(v => v.value.toInt)
}
