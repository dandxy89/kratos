package com.dandxy.golf.input

import java.sql.Timestamp

import doobie.util.Meta
import io.circe.Encoder
import io.circe.syntax._
import io.circe.generic.semiauto.deriveEncoder

final case class Handicap(value: Double) extends AnyVal

object Handicap {
  // Instances
  implicit val meta: Meta[Handicap]  = Meta[Double].imap(Handicap(_))(_.value)
  implicit val en: Encoder[Handicap] = Encoder.instance(_.value.asJson)
}

final case class HandicapWithDate(value: Handicap, dt: Timestamp)

object HandicapWithDate {
  import com.dandxy.util.Codecs.TimestampFormat
  implicit val en: Encoder[HandicapWithDate] = deriveEncoder
}
