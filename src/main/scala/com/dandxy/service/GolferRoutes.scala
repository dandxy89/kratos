package com.dandxy.service

import cats.effect.Concurrent
import cats.syntax.all._
import com.dandxy.db.UserStore
import com.dandxy.golf.entity.Location
import com.dandxy.golf.input.GolfInput.{ UserGameInput, UserShotInput }
import com.dandxy.golf.input.{ Distance, Handicap }
import com.dandxy.golf.pga.Statistic.PGAStatistic
import com.dandxy.jwt.Claims
import com.dandxy.middleware.http4s.content.defaults._
import com.dandxy.model.error.DomainError._
import com.dandxy.model.player.PlayerId
import com.dandxy.model.user.GolfClubData
import com.dandxy.model.user.Identifier.{ GameId, Hole }
import com.dandxy.service.GolfResultService.{ processGolfResult, processHoleResult }
import com.dandxy.util.RouteUtils.runDbOp
import com.dandxy.strokes.GolfResult
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.AuthMiddleware
import org.http4s.{ AuthedRoutes, HttpRoutes }

import scala.language.higherKinds

class GolferRoutes[F[_]](us: UserStore[F],
                         middleware: AuthMiddleware[F, Claims],
                         getStatistic: (Distance, Location) => F[Option[PGAStatistic]])(implicit F: Concurrent[F])
    extends Http4sDsl[F] {

  val defaultHandicap: Handicap                                                      = Handicap(0)
  val processingGolfResult: (GameId, Boolean) => F[GolfResult]                       = processGolfResult(us, getStatistic)
  val processingHoleResult: (GameId, Option[Hole], Boolean) => F[Option[GolfResult]] = processHoleResult(us, getStatistic)

  object OverwriteQueryParam extends OptionalQueryParamDecoderMatcher[Boolean]("overwrite")

  private val routes: AuthedRoutes[Claims, F] = AuthedRoutes.of[Claims, F] {

    case authReq @ PUT -> Root / "club" as id =>
      val op = authReq.req.as[List[GolfClubData]].flatMap(in => us.addClubData(PlayerId(id.playerId), in))
      runDbOp(op, InvalidDataProvided, authReq.req)

    case authReq @ GET -> Root / "club" as id =>
      runDbOp(us.getUserClubs(PlayerId(id.playerId)), InvalidPlayerProvided, authReq.req)

    case authReq @ GET -> Root / "game" / "all" as id =>
      runDbOp(us.getAllPlayerGames(PlayerId(id.playerId)), InvalidPlayerProvided, authReq.req)

    case authReq @ GET -> Root / "game" / IntVar(gameId) as _ =>
      runDbOp(us.getPlayerGame(GameId(gameId)), InvalidGameProvided, authReq.req)

    case authReq @ PUT -> Root / "game" as _ =>
      runDbOp(authReq.req.as[UserGameInput] >>= us.addPlayerGame, InvalidDataProvided, authReq.req)

    case authReq @ DELETE -> Root / "game" / IntVar(gameId) as _ =>
      runDbOp(us.deletePlayerGame(GameId(gameId)), InvalidPlayerProvided, authReq.req)

    case authReq @ PUT -> Root / "game" / "shot" as _ =>
      runDbOp(authReq.req.as[List[UserShotInput]] >>= us.addPlayerShots, InvalidDataProvided, authReq.req)

    case authReq @ GET -> Root / "game" / IntVar(gameId) / "shot" as _ =>
      runDbOp(us.getByGameAndMaybeHole(GameId(gameId), None), InvalidPlayerProvided, authReq.req)

    case authReq @ GET -> Root / "game" / IntVar(gameId) / "shot" / "hole" / IntVar(holeId) as _ =>
      runDbOp(us.getByGameAndMaybeHole(GameId(gameId), Option(Hole(holeId))), InvalidPlayerProvided, authReq.req)

    case authReq @ GET -> Root / "handicap" as id =>
      runDbOp(us.getHandicapHistory(PlayerId(id.playerId)), InvalidPlayerProvided, authReq.req)

    case authReq @ GET -> Root / "result" / IntVar(gameId) :? OverwriteQueryParam(b) as _ =>
      runDbOp(processingGolfResult(GameId(gameId), b.getOrElse(false)), InvalidGameProvided, authReq.req)

    case authReq @ GET -> Root / "result" / IntVar(gameId) / "hole" / IntVar(holeId) :? OverwriteQueryParam(b) as _ =>
      runDbOp(processingHoleResult(GameId(gameId), Option(Hole(holeId)), b.getOrElse(false)), InvalidGameProvided, authReq.req)

    case authReq @ GET -> Root / "aggregate" / IntVar(gameId) as _ =>
      runDbOp(us.aggregateGameResult(GameId(gameId)), InvalidPlayerProvided, authReq.req)

    case authReq @ GET -> Root / "statistic" / "location" / IntVar(location) / "distance" / IntVar(distance) as _ =>
      runDbOp(getStatistic(Distance(distance.toDouble), Location.fromId(location)), InvalidPlayerProvided, authReq.req)

  }

  val golferRoutes: HttpRoutes[F] = middleware(routes)

}

object GolferRoutes {

  def apply[F[_]](userStore: UserStore[F],
                  middleware: AuthMiddleware[F, Claims],
                  getStatistic: (Distance, Location) => F[Option[PGAStatistic]])(implicit F: Concurrent[F]) =
    new GolferRoutes[F](userStore, middleware, getStatistic)

}
