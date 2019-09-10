package com.dandxy.service

import cats.effect.Sync
import cats.implicits._
import com.dandxy.db.UserStore
import com.dandxy.jwt.{ Claims, JwtAuthMiddleware }
import com.dandxy.middleware.http4s.content.defaults._
import com.dandxy.middleware.http4s.content.syntax._
import com.dandxy.model.error.DomainError
import com.dandxy.model.error.DomainError.InvalidPlayerProvided
import com.dandxy.model.player.PlayerId
import com.dandxy.util.Codecs._
import org.http4s.{ AuthedRoutes, HttpRoutes }
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.AuthMiddleware
import pdi.jwt.JwtAlgorithm

import scala.language.higherKinds

class GolferRoutes[F[_]](userStore: UserStore[F], secretKey: String)(implicit F: Sync[F]) extends Http4sDsl[F] {

  val middleware: AuthMiddleware[F, Claims] = JwtAuthMiddleware[F, Claims](secretKey, Seq(JwtAlgorithm.HS256))

  private val routes: AuthedRoutes[Claims, F] = AuthedRoutes.of[Claims, F] {

    // PUT /golfer/club + Active JWT Token + JSON payload
    case _ @PUT -> Root / "club" as _ => ???

    case authReq @ GET -> Root / "club" as id =>
      userStore
        .getUserClubs(PlayerId(id.playerId))
        .attempt
        .map(_.leftMap[DomainError](_ => InvalidPlayerProvided))
        .negotiate(authReq.req)

    case authReq @ GET -> Root / "game" / "all" as id =>
      userStore
        .getAllPlayerGames(PlayerId(id.playerId))
        .attempt
        .map(_.leftMap[DomainError](_ => InvalidPlayerProvided))
        .negotiate(authReq.req)

    // GET /golf/game/{id} + Active JWT Token
    case GET -> Root / "game" / _ as _ => ???
//      val res = for {
//        _     <- F.fromEither[PlayerId](id.asPlayerId)
//        clubs <- userStore.getPlayerGame(id)
//      } yield clubs
//
//      res
//        .attempt
//        .map(_.leftMap[DomainError](_ => InvalidPlayerProvided))
//        .negotiate(authReq.req)

    // PUT /golf/game/{id} + Active JWT Token - this will generate a new game id
    case _ @PUT -> Root / "game" / _ as _ => ???

    // DELETE /golf/game/{id} + Active JWT Token - this will generate a new game id
    case DELETE -> Root / "game" / _ as _ => ???

    // GET /golf/game/{id}/hole/{optional id} + Active JWT Token - this will get all of the shot data from the database
    case GET -> Root / "game" / _ / "hole" / _ as _ => ???

    // PUT /golf/game/shot + Active JWT Token + JSON payload of shots - this will add new shots / overwrite where required
    case _ @PUT -> Root / "game" / "shot" as _ => ???

    // DELETE /golf/game/{id}/hole/{id} + Active JWT Token - this will delete shots by hole id
    case DELETE -> Root / "game" / _ / "hole" / _ as _ => ???

    case authReq @ GET -> Root / "handicap" as id =>
      userStore
        .getHandicapHistory(PlayerId(id.playerId))
        .attempt
        .map(_.leftMap[DomainError](_ => InvalidPlayerProvided))
        .negotiate(authReq.req)

    // GET /golf/result/{game_id} + Active JWT Token - this will generate some summary statistics for a given game
    case GET -> Root / "result" / _ as _ => ???

    // GET /golf/result/{game_id}/hole/{hole} + Active JWT Token - this will generate some summary statistics for a given game
    case GET -> Root / "result" / _ / "hole" / _ as _ => ???

  }

  val golferRoutes: HttpRoutes[F] = middleware(routes)

}

object GolferRoutes {

  def apply[F[_]: Sync](userStore: UserStore[F], secretKey: String) =
    new GolferRoutes[F](userStore, secretKey)
}
