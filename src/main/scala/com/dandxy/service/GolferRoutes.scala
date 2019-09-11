package com.dandxy.service

import cats.{ Applicative, Monad, MonadError }
import cats.effect.Sync
import cats.syntax.flatMap._
import com.dandxy.db.UserStore
import com.dandxy.jwt.{ Claims, JwtAuthMiddleware }
import com.dandxy.middleware.http4s.ToHttpResponse
import com.dandxy.middleware.http4s.content.defaults._
import com.dandxy.middleware.http4s.content.syntax._
import com.dandxy.model.error.DomainError
import com.dandxy.model.error.DomainError._
import com.dandxy.model.player.PlayerId
import com.dandxy.model.user.Identifier.{ GameId, Hole }
import com.dandxy.util.Codecs._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.AuthMiddleware
import org.http4s.{ AuthedRoutes, HttpRoutes, Request, Response }
import pdi.jwt.JwtAlgorithm

import scala.language.higherKinds

class GolferRoutes[F[_]: Applicative](us: UserStore[F], secretKey: String)(implicit F: Sync[F]) extends Http4sDsl[F] {

  val middleware: AuthMiddleware[F, Claims] = JwtAuthMiddleware[F, Claims](secretKey, Seq(JwtAlgorithm.HS256))

  def runDbOp[B](op: F[B], e: DomainError, r: Request[F])(implicit ME: MonadError[F, Throwable],
                                                          c1: ToHttpResponse[F, B]): F[Response[F]] =
    ME.attempt[B](op).flatMap {
      case Right(value) => value.negotiate(r)
      case Left(_)      => e.negotiate(r)(errorResponse, Monad[F])
    }

  private val routes: AuthedRoutes[Claims, F] = AuthedRoutes.of[Claims, F] {

    // PUT /golfer/club + Active JWT Token + JSON payload
    case _ @PUT -> Root / "club" as _ => ???
    //runDbOp(us.addClubData(PlayerId(id.playerId), ???), InvalidDataProvided, authReq.req)

    case authReq @ GET -> Root / "club" as id =>
      runDbOp(us.getUserClubs(PlayerId(id.playerId)), InvalidPlayerProvided, authReq.req)

    case authReq @ GET -> Root / "game" / "all" as id =>
      runDbOp(us.getAllPlayerGames(PlayerId(id.playerId)), InvalidPlayerProvided, authReq.req)

    case authReq @ GET -> Root / "game" / IntVar(gameId) as _ =>
      runDbOp(us.getPlayerGame(GameId(gameId)), InvalidPlayerProvided, authReq.req)

    // PUT /golf/game/{id} + Active JWT Token
    case _ @PUT -> Root / "game" / IntVar(_) as _ => ???
    //runDbOp(us.addPlayerGame(???), InvalidPlayerProvided, authReq.req)

    case authReq @ DELETE -> Root / "game" / IntVar(gameId) as _ =>
      runDbOp(us.deletePlayerGame(GameId(gameId)), InvalidPlayerProvided, authReq.req)

    // GET /golf/game/{id}?hole=optional id + Active JWT Token
    case authReq @ GET -> Root / "game" / IntVar(gameId) as _ =>
      runDbOp(us.getPlayerGame(GameId(gameId)), InvalidPlayerProvided, authReq.req)

    // PUT /golf/game/shot + Active JWT Token + JSON payload of shots
    case _ @PUT -> Root / "game" / "shot" as _ => ???
    // runDbOp(us.addPlayerShots(???), InvalidPlayerProvided, authReq.req)

    case authReq @ GET -> Root / "handicap" as id =>
      runDbOp(us.getHandicapHistory(PlayerId(id.playerId)), InvalidPlayerProvided, authReq.req)

    case authReq @ GET -> Root / "result" / IntVar(gameId) as _ =>
      runDbOp(us.getResultByIdentifier(GameId(gameId), None), InvalidPlayerProvided, authReq.req)

    case authReq @ GET -> Root / "result" / IntVar(gameId) / "hole" / IntVar(holeId) as _ =>
      runDbOp(us.getResultByIdentifier(GameId(gameId), Option(Hole(holeId))), InvalidPlayerProvided, authReq.req)

  }

  val golferRoutes: HttpRoutes[F] = middleware(routes)

}

object GolferRoutes {

  def apply[F[_]: Sync](userStore: UserStore[F], secretKey: String) =
    new GolferRoutes[F](userStore, secretKey)
}
