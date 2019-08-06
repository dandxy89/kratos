package com.dandxy.model.golf.input

import doobie.util.Meta

final case class Distance(value: Double) extends AnyVal

object Distance {

  implicit val meta: Meta[Distance] = Meta[Double].imap(Distance(_))(_.value)

}
