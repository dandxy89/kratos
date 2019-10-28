package com.dandxy.model.player

import com.dandxy.auth.Password
import com.dandxy.model.user.UserRegistration
import io.circe.generic.semiauto._
import io.circe.syntax._
import io.circe.{ Decoder, Encoder }

final case class Registration(user: UserRegistration, password: Password)

object Registration {
  implicit val e2: Encoder[Password] = Encoder.instance(_.value.asJson)
  implicit val d2: Decoder[Password] = Decoder.instance(h => h.as[String].map(Password))

  implicit val e: Encoder[Registration] = deriveEncoder
  implicit val d: Decoder[Registration] = deriveDecoder
}
