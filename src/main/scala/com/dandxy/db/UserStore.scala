package com.dandxy.db

import java.sql.Timestamp

import cats.effect.Bracket
import com.dandxy.auth.{ PasswordAuth, PlayerHash }
import com.dandxy.config.AppModels.AuthSalt
import com.dandxy.db.sql.TableName._
import com.dandxy.model.golf.entity.Hole
import com.dandxy.model.golf.input.GolfInput.{ UserGameInput, UserShotInput }
import com.dandxy.model.golf.input.HandicapWithDate
import com.dandxy.model.user._
import doobie._
import doobie.implicits._

import scala.language.higherKinds

trait UserStore[F[_]] {
  def gdprPurge(playerId: PlayerId): F[Int]
  def registerUser(registration: UserRegistration, hashedPassword: Password, updateTime: Timestamp): F[PlayerId]
  def attemptLogin(email: UserEmail, rawPassword: Password): F[Option[PlayerId]]
  def addClubData(playerId: PlayerId, input: List[GolfClubData]): F[Int]
  def getUserClubs(playerId: PlayerId): F[List[GolfClubData]]
  def getAllPlayerGames(playerId: PlayerId): F[List[UserGameInput]]
  def getPlayerGame(gameId: GameId): F[Option[UserGameInput]]
  def addPlayerGame(game: UserGameInput): F[GameId]
  def dropByHole(gameId: GameId, hole: Hole): F[Int]
  def addPlayerShots(input: List[UserShotInput]): F[Int]
  def getByGameAndMaybeHole(gameId: GameId, hole: Option[Hole]): F[List[UserShotInput]]
  def getHandicapHistory(playerId: PlayerId): F[List[HandicapWithDate]]
  def aggregateGameResult(gameId: GameId): F[List[AggregateGameResult]]
}

class UserPostgresQueryTool[F[_]](xa: Transactor[F], config: AuthSalt)(implicit F: Bracket[F, Throwable]) extends UserStore[F] {

  import com.dandxy.db.sql.UserQueryToolSQL._

  def gdprPurge(playerId: PlayerId): F[Int] =
    (
      for {
        s <- purgeShotsByPlayerId(playerId, PlayerShot.name)
        g <- purgeByPlayerId(playerId, PlayerGame.name)
        c <- purgeByPlayerId(playerId, PlayerClubData.name)
        p <- purgeByPlayerId(playerId, UserSecurity.name)
        l <- purgeByPlayerId(playerId, PlayerLookup.name)
      } yield g + c + p + l + s
    ).transact(xa)

  def registerUser(registration: UserRegistration, hashedPassword: Password, updateTime: Timestamp): F[PlayerId] =
    (
      for {
        id <- addUser(registration, updateTime)
        _  <- addHashedPassword(registration.email, hashedPassword, PlayerId(id))
      } yield PlayerId(id)
    ).transact(xa)

  def attemptLogin(email: UserEmail, rawPassword: Password): F[Option[PlayerId]] =
    (
      checkLogin(email).map {
        case None                    => None
        case Some(PlayerHash(p, ph)) => if (PasswordAuth.verify(rawPassword, ph.value, config.salt)) Some(p) else None
      }
    ).transact(xa)

  def addClubData(playerId: PlayerId, input: List[GolfClubData]): F[Int] =
    (
      for {
        _ <- purgeByPlayerId(playerId, PlayerClubData.name)
        c <- insertClubData(input)
      } yield c
    ).transact(xa)

  def getUserClubs(playerId: PlayerId): F[List[GolfClubData]] =
    fetchClubData(playerId).transact(xa)

  def getAllPlayerGames(playerId: PlayerId): F[List[UserGameInput]] =
    fetchAllPlayerGames(playerId).transact(xa)

  def getPlayerGame(gameId: GameId): F[Option[UserGameInput]] =
    fetchPlayerGame(gameId).transact(xa)

  def addPlayerGame(game: UserGameInput): F[GameId] =
    insertPlayerGame(game).transact(xa)

  def dropByHole(gameId: GameId, hole: Hole): F[Int] =
    dropShotsByHole(gameId, hole).transact(xa)

  def addPlayerShots(input: List[UserShotInput]): F[Int] =
    insertPlayerShots(input).transact(xa)

  def getByGameAndMaybeHole(gameId: GameId, hole: Option[Hole]): F[List[UserShotInput]] =
    fetchPlayerShot(gameId, hole).transact(xa)

  def getHandicapHistory(playerId: PlayerId): F[List[HandicapWithDate]] =
    fetchHandicapHistory(playerId).transact(xa)

  def aggregateGameResult(gameId: GameId): F[List[AggregateGameResult]] =
    fetchAggregateGameResult(gameId).transact(xa)
}
