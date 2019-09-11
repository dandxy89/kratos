package com.dandxy.golf.input

import java.sql.Timestamp

import doobie.util.Meta
import io.circe.{ Decoder, Encoder }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.syntax._

final case class Handicap(value: Double) extends AnyVal

object Handicap {
  // Instances
  implicit val meta: Meta[Handicap]  = Meta[Double].imap(Handicap(_))(_.value)
  implicit val en: Encoder[Handicap] = Encoder.instance(_.value.asJson)
  implicit val de: Decoder[Handicap] = Decoder.instance(_.as[Double].map(Handicap(_)))
}

final case class HandicapWithDate(value: Handicap, dt: Timestamp)

object HandicapWithDate {
  // Instances
  import com.dandxy.util.Codecs.TimestampFormat
  implicit val en: Encoder[HandicapWithDate] = deriveEncoder
  implicit val de: Decoder[HandicapWithDate] = deriveDecoder
}
