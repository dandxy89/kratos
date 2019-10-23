package com.dandxy.service

import cats.MonadError
import cats.effect.Concurrent
import cats.syntax.all._
import com.dandxy.db.MetricsStore
import com.dandxy.jwt.Claims
import com.dandxy.middleware.http4s.ToHttpResponse
import com.dandxy.middleware.http4s.content.defaults._
import com.dandxy.middleware.http4s.content.syntax._
import com.dandxy.model.error.DomainError
import com.dandxy.model.error.DomainError._
import com.dandxy.model.user.Identifier.GameId
import com.dandxy.util.Codecs._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.AuthMiddleware
import org.http4s.{AuthedRoutes, HttpRoutes, Request, Response}

import scala.language.higherKinds

class MetricsRoutes[F[_]](ms: MetricsStore[F], middleware: AuthMiddleware[F, Claims])(implicit F: Concurrent[F]) extends Http4sDsl[F] {

  def runDbOp[A](op: F[A], e: DomainError, r: Request[F])(implicit ME: MonadError[F, Throwable],
                                                          c: ToHttpResponse[F, A]): F[Response[F]] =
    ME.attempt(op).flatMap {
      case Right(value) => value.negotiate(r)
      case Left(error)      => 
        println(error.getMessage())
        e.negotiate(r)
    }

  private val routes: AuthedRoutes[Claims, F] = AuthedRoutes.of[Claims, F] {

    case authReq @ GET -> Root / "standard" / IntVar(gameId) as _ =>
      runDbOp(ms.standardScores(GameId(gameId)), InvalidGameProvided, authReq.req)

    case authReq @ GET -> Root / "fir" / IntVar(gameId) as _ =>
      runDbOp(ms.fairwaysInRegulation(GameId(gameId)), InvalidGameProvided, authReq.req)

    case authReq @ GET -> Root / "gir" / IntVar(gameId) as _ =>
      runDbOp(ms.greenInRegulation(GameId(gameId)), InvalidGameProvided, authReq.req)

    case authReq @ GET -> Root / "putting" / "round" / IntVar(gameId) as _ =>
      runDbOp(ms.puttsPerRound(GameId(gameId)), InvalidGameProvided, authReq.req)

    case authReq @ GET -> Root / "putting" / IntVar(gameId) / IntVar(bucketSize) / IntVar(limit) as _ =>
      runDbOp(ms.puttsToHoleDistance(GameId(gameId), bucketSize, limit), InvalidGameProvided, authReq.req)

    case authReq @ GET -> Root / "penalties" / IntVar(gameId) as _ =>
      runDbOp(ms.penaltiesPerRound(GameId(gameId)), InvalidGameProvided, authReq.req)

    case authReq @ GET -> Root / "strokes_gained" / "shots" / "best" / IntVar(gameId) / IntVar(count) as _ =>
      runDbOp(ms.bestXShots(GameId(gameId), count), InvalidGameProvided, authReq.req)

    case authReq @ GET -> Root / "strokes_gained" / "shots" / "worst" / IntVar(gameId) / IntVar(count) as _ =>
      runDbOp(ms.worstXShots(GameId(gameId), count), InvalidGameProvided, authReq.req)

    case authReq @ GET -> Root / "strokes_gained" / "club" / IntVar(gameId) as _ =>
      runDbOp(ms.strokesGainedByClub(GameId(gameId)), InvalidGameProvided, authReq.req)

    case authReq @ GET -> Root / "strokes_gained" / "without" / IntVar(gameId) / IntVar(count) as _ =>
      runDbOp(ms.strokesGainedWithoutXShots(GameId(gameId), count), InvalidGameProvided, authReq.req)

    case authReq @ GET -> Root / "club" / "distance" / IntVar(gameId) as _ =>
      runDbOp(ms.averageDistanceByClub(GameId(gameId)), InvalidGameProvided, authReq.req)

    case authReq @ GET -> Root / "driving" / "distance" / IntVar(gameId) as _ =>
      runDbOp(ms.averageDriveLength(GameId(gameId)), InvalidGameProvided, authReq.req)

  }

  val metricsRoutes: HttpRoutes[F] = middleware(routes)

}

object MetricsRoutes {

  def apply[F[_]](ms: MetricsStore[F], middleware: AuthMiddleware[F, Claims])(implicit F: Concurrent[F]) =
    new MetricsRoutes[F](ms, middleware)

}
