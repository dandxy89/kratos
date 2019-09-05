package com.dandxy.service

import cats.effect.Sync
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

import scala.language.higherKinds

class GolferRoutes[F[_]]()(implicit F: Sync[F]) extends Http4sDsl[F] {

  // TOKEN AUTHENTICATION

  val golferRoutes: HttpRoutes[F] = HttpRoutes.of[F] {
    // PUT /golfer/register + JSON payload
    case _ @GET -> Root / "register" => ???

    // GET /golfer/verify?token={Emailed JWT Token}
    case GET -> Root / "verify" => ???

    // PUT /golfer/club/update + Active JWT Token + JSON payload
    case _ @PUT -> Root / "club" => ???

    // GET /golfer/club/update + Active JWT Token
    case GET -> Root / "club" => ???

    // GET /golfer/game/all?page={offset}
    case GET -> Root / "game" / all => ???

    // GET /golf/game/{id} + Active JWT Token
    case GET -> Root / "game" => ???

    // PUT /golf/game/{id} + Active JWT Token - this will generate a new game id
    case _ @PUT -> Root / "game" => ???

    // DELETE /golf/game/{id} + Active JWT Token - this will generate a new game id
    case DELETE -> Root / "game" / id => ???

    // GET /golf/game/{id}/hole/{optional id} + Active JWT Token - this will get all of the shot data from the database
    case GET -> Root / "game" / gameId / "hole" / id => ???

    // PUT /golf/game/shot + Active JWT Token + JSON payload of shots - this will add new shots / overwrite where required
    case _ @PUT -> Root / "game" / "shot" => ???

    // DELETE /golf/game/{id}/hole/{id} + Active JWT Token - this will delete shots by hole id
    case DELETE -> Root / "game" / gameId / "hole" / id => ???

    // GET /golf/handicap/{player_id} + Active JWT Token - this will get get all handicaps recorded for the player
    case GET -> Root / "handicap" / playerId => ???

    // GET /golf/result/{game_id} + Active JWT Token - this will generate some summary statistics for a given game
    case GET -> Root / "result" / gameId => ???

    // GET /golf/result/{game_id}/hole/{hole} + Active JWT Token - this will generate some summary statistics for a given game
    case GET -> Root / "result" / gameId / "hole" / holeId => ???

  }

}

object GolferRoutes {
  def apply[F[_]: Sync]() = new GolferRoutes[F]()
}
