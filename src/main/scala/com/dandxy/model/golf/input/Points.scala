package com.dandxy.model.golf.input

import doobie.util.Meta

final case class Points(value: Int)

object Points {
  implicit val meta: Meta[Points] = Meta[Int].imap(Points(_))(_.value)
}
