package com.dandxy.model.user

import doobie.util.Meta
import io.circe.syntax._
import io.circe.{ Decoder, Encoder }

final case class Password(value: String) extends AnyVal

object Password {
  implicit val meta: Meta[Password] = Meta[String].imap(Password(_))(_.value)
  implicit val e: Encoder[Password] = Encoder.instance(_.value.asJson)
  implicit val d: Decoder[Password] = Decoder.instance(h => h.as[String].map(Password(_)))
}
