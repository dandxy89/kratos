package com.dandxy.model.golf.input

import doobie.util.Meta

final case class Handicap(value: Double) extends AnyVal

object Handicap {

  implicit val meta: Meta[Handicap] = Meta[Double].imap(Handicap(_))(_.value)

}
