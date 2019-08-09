package com.dandxy.model.golf.input

import java.sql.Timestamp

import doobie.util.Meta

final case class Handicap(value: Double) extends AnyVal

object Handicap {

  implicit val meta: Meta[Handicap] = Meta[Double].imap(Handicap(_))(_.value)

}

final case class HandicapWithDate(value: Handicap, dt: Timestamp)
