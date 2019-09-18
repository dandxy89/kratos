package com.dandxy.service

import cats.effect.Sync
import cats.syntax.all._
import cats.MonadError
import cats.instances.list._
import com.dandxy.db.UserStore
import com.dandxy.golf.input.GolfInput.{ UserGameInput, UserShotInput }
import com.dandxy.golf.input.Handicap
import com.dandxy.jwt.{ Claims, JwtAuthMiddleware }
import com.dandxy.middleware.http4s.ToHttpResponse
import com.dandxy.middleware.http4s.content.defaults._
import com.dandxy.middleware.http4s.content.syntax._
import com.dandxy.model.error.DomainError
import com.dandxy.model.error.DomainError._
import com.dandxy.model.player.PlayerId
import com.dandxy.model.user.GolfClubData
import com.dandxy.model.user.Identifier.{ GameId, Hole }
import com.dandxy.strokes.GolfResult
import com.dandxy.golf.entity.Location
import com.dandxy.golf.input.Distance
import com.dandxy.strokes.StrokesGainedCalculator.calculateMany
import com.dandxy.golf.pga.Statistic.PGAStatistic
import com.dandxy.util.Codecs._
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.AuthMiddleware
import org.http4s.{ AuthedRoutes, HttpRoutes, Request, Response }
import pdi.jwt.JwtAlgorithm

import scala.language.higherKinds

class GolferRoutes[F[_]](us: UserStore[F], secretKey: String, getStatistic: (Distance, Location) => F[Option[PGAStatistic]])(
  implicit F: Sync[F]
) extends Http4sDsl[F] {

  val middleware: AuthMiddleware[F, Claims] = JwtAuthMiddleware[F, Claims](secretKey, Seq(JwtAlgorithm.HS256))
  val defaultHandicap: Handicap             = Handicap(0)

  def runDbOp[A](op: F[A], e: DomainError, r: Request[F])(implicit ME: MonadError[F, Throwable],
                                                          c: ToHttpResponse[F, A]): F[Response[F]] =
    ME.attempt(op).flatMap {
      case Right(value) => value.negotiate(r)
      case Left(_)      => e.negotiate(r)
    }

  def processGolfResult(g: GameId, h: Option[Hole], overwrite: Boolean): F[List[GolfResult]] =
    us.getResultByIdentifier(g, h).flatMap {
      case Some(value) if !overwrite => List(value).pure[F]
      case _ =>
        (us.getByGameAndMaybeHole(g, h), us.getGameHandicap(g)).mapN { (in, hd) =>
          for {
            r <- calculateMany[F](getStatistic)(hd.getOrElse(defaultHandicap), in)
            _ <- r.traverse(sg => us.addPlayerShots(sg.shots)) *> r.traverse(sg => us.addResultByIdentifier(sg.result, h))
          } yield r.map(_.result)
        }.flatten
    }

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
      runDbOp(processGolfResult(GameId(gameId), None, b.getOrElse(false)), InvalidGameProvided, authReq.req)

    case authReq @ GET -> Root / "result" / IntVar(gameId) / "hole" / IntVar(holeId) :? OverwriteQueryParam(b) as _ =>
      runDbOp(processGolfResult(GameId(gameId), Option(Hole(holeId)), b.getOrElse(false)), InvalidGameProvided, authReq.req)

  }

  val golferRoutes: HttpRoutes[F] = middleware(routes)

}

object GolferRoutes {

  def apply[F[_]](userStore: UserStore[F], secretKey: String, getStatistic: (Distance, Location) => F[Option[PGAStatistic]])(
    implicit F: Sync[F]
  ) =
    new GolferRoutes[F](userStore, secretKey, getStatistic)

}
