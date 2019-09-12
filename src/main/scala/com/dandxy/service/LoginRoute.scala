package com.dandxy.service

import cats.effect.Sync
import cats.implicits._
import com.dandxy.model.player.PlayerId
import com.dandxy.model.user.{ Password, UserEmail }
import org.http4s.dsl.Http4sDsl
import org.http4s.server.AuthMiddleware
import org.http4s.server.middleware.authentication.BasicAuth
import org.http4s.server.middleware.authentication.BasicAuth.BasicAuthenticator
import org.http4s.{ AuthedRoutes, BasicCredentials, Header, HttpRoutes, Response, Status }

import scala.language.higherKinds

class LoginRoute[F[_]](authenticator: BasicAuthenticator[F, PlayerId], newToken: PlayerId => String)(implicit F: Sync[F])
    extends Http4sDsl[F] {

  private val realm = "golfApp"

  val basicAuth: AuthMiddleware[F, PlayerId] = BasicAuth(realm, authenticator)

  def loginRoute: HttpRoutes[F] =
    basicAuth(AuthedRoutes.of[PlayerId, F] {
      case GET -> Root / "golfer" as id =>
        Response[F](Status.Ok)
        .withHeaders(Header("Authorization", newToken(id))).pure[F]
    })
}

object LoginRoute {

  def apply[F[_]: Sync](attemptLogin: (UserEmail, Password) => F[Option[PlayerId]], newToken: PlayerId => String): LoginRoute[F] = {

    val authenticator: BasicAuthenticator[F, PlayerId] =
      (cred: BasicCredentials) => attemptLogin(UserEmail(cred.username), Password(cred.password))

    new LoginRoute[F](authenticator, newToken)
  }
}
