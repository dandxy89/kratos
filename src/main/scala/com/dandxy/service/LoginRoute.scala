package com.dandxy.service

import cats.effect.Sync
import com.dandxy.model.player.PlayerId
import com.dandxy.model.user.{ Password, UserEmail }
import org.http4s.dsl.Http4sDsl
import org.http4s.server.AuthMiddleware
import org.http4s.server.middleware.authentication.BasicAuth
import org.http4s.server.middleware.authentication.BasicAuth.BasicAuthenticator
import org.http4s.{ AuthedRoutes, BasicCredentials, HttpRoutes }
import pdi.jwt.JwtClaim

import scala.language.higherKinds

class LoginRoute[F[_]](authenticator: BasicAuthenticator[F, PlayerId], newToken: PlayerId => JwtClaim)(implicit F: Sync[F])
    extends Http4sDsl[F] {

  private val realm = "golfApp"

  val basicAuth: AuthMiddleware[F, PlayerId] = BasicAuth(realm, authenticator)

  def loginRoute: HttpRoutes[F] =
    basicAuth(AuthedRoutes.of[PlayerId, F] {
      case GET -> Root / "login" as id => Ok(newToken(id).toJson)
    })
}

object LoginRoute {
  def apply[F[_]: Sync](attemptLogin: (UserEmail, Password) => F[Option[PlayerId]], newToken: PlayerId => JwtClaim): LoginRoute[F] = {

    val authenticator: BasicAuthenticator[F, PlayerId] =
      (cred: BasicCredentials) => attemptLogin(UserEmail(cred.username), Password(cred.password))

    new LoginRoute[F](authenticator, newToken)
  }
}
