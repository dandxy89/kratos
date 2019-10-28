package com.dandxy.service

import java.sql.Timestamp
import java.time.LocalDateTime

import cats.Monad
import cats.effect.Sync
import cats.implicits._
import com.dandxy.auth.Password
import com.dandxy.middleware.http4s.content.defaults._
import com.dandxy.middleware.http4s.content.syntax._
import com.dandxy.model.error.DomainError
import com.dandxy.model.error.DomainError.InvalidDataProvided
import com.dandxy.model.player.{PlayerId, Registration}
import com.dandxy.model.user.UserRegistration
import com.dandxy.util.Codecs._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.Http4sDsl

import scala.language.higherKinds

class RegistrationRoute[F[_]: Monad](registerUser: (UserRegistration, Password, Timestamp) => F[PlayerId])(implicit F: Sync[F])
    extends Http4sDsl[F] {

  val registrationRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "golfer" =>
      val res: F[PlayerId] = for {
        d <- req.as[Registration]
        a <- F
          .delay(LocalDateTime.now())
          .flatMap(t => registerUser(d.user, d.password, Timestamp.valueOf(t)))
      } yield a

      res
        .attempt
        .map(_.leftMap[DomainError](_ => InvalidDataProvided))
        .negotiate(req)
  }
}

object RegistrationRoute {

  def apply[F[_]: Sync](registerUser: (UserRegistration, Password, Timestamp) => F[PlayerId]) =
    new RegistrationRoute[F](registerUser)
}
