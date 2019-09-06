package com.dandxy.model.user

import io.circe.syntax._
import io.circe.{ Decoder, Encoder, Json }

final case class UserRegistration(email: UserEmail, firstName: String, lastName: String)

object UserRegistration {

  implicit val e: Encoder[UserRegistration] = Encoder.instance { c =>
    Json.obj(
      "email"     -> c.email.email.asJson,
      "firstName" -> c.firstName.asJson,
      "lastName"  -> c.lastName.asJson
    )
  }

  implicit val d: Decoder[UserRegistration] = Decoder.instance { c =>
    for {
      e <- c.downField("email").as[String]
      f <- c.downField("firstName").as[String]
      l <- c.downField("lastName").as[String]
    } yield UserRegistration(UserEmail(e), f, l)
  }
}
