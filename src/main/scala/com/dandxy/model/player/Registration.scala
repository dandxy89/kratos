package com.dandxy.model.player

import cats.effect.Sync
import com.dandxy.model.user.{ Password, UserRegistration }
import io.circe.generic.semiauto._
import io.circe.{ Decoder, Encoder }
import org.http4s.circe.{ jsonEncoderOf, jsonOf }
import org.http4s.{ EntityDecoder, EntityEncoder }

import scala.language.higherKinds

final case class Registration(user: UserRegistration, password: Password)

object Registration {
  implicit val e: Encoder[Registration] = deriveEncoder
  implicit val d: Decoder[Registration] = deriveDecoder

  implicit def ed[F[_]: Sync]: EntityDecoder[F, Registration] = jsonOf
  implicit def ee[F[_]: Sync]: EntityEncoder[F, Registration] = jsonEncoderOf
}
