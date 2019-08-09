package com.dandxy.db

import java.sql.Timestamp

import cats.effect.IO
import com.dandxy.model.golf.entity.{ Hole, PGAStatistics }
import com.dandxy.model.golf.input.GolfInput.{ UserGameInput, UserShotInput }
import com.dandxy.model.golf.input.{ Distance, HandicapWithDate }
import com.dandxy.model.golf.pga.Statistic.PGAStatistic
import com.dandxy.model.user._

class DBOperations(pgaOps: PGAPostgresQueryTool[IO], userOps: UserPostgresQueryTool[IO]) extends PGAStore[IO] with UserStore[IO] {

  override def getStatistic(distance: Distance, stat: PGAStatistics): IO[Option[PGAStatistic]] =
    pgaOps.getStatistic(distance, stat)

  override def gdprPurge(playerId: PlayerId): IO[Int] =
    userOps.gdprPurge(playerId)

  override def registerUser(registration: UserRegistration, hashedPassword: Password, updateTime: Timestamp): IO[PlayerId] =
    userOps.registerUser(registration, hashedPassword, updateTime)

  override def attemptLogin(email: UserEmail, rawPassword: Password): IO[Option[PlayerId]] =
    userOps.attemptLogin(email, rawPassword)

  override def addClubData(playerId: PlayerId, input: List[GolfClubData]): IO[Int] =
    userOps.addClubData(playerId, input)

  override def getUserClubs(playerId: PlayerId): IO[List[GolfClubData]] =
    userOps.getUserClubs(playerId)

  override def getAllPlayerGames(playerId: PlayerId): IO[List[UserGameInput]] =
    userOps.getAllPlayerGames(playerId)

  override def getPlayerGame(gameId: GameId): IO[Option[UserGameInput]] =
    userOps.getPlayerGame(gameId)

  override def addPlayerGame(game: UserGameInput): IO[GameId] =
    userOps.addPlayerGame(game)

  override def dropByHole(gameId: GameId, hole: Hole): IO[Int] =
    userOps.dropByHole(gameId, hole)

  override def addPlayerShots(input: List[UserShotInput]): IO[Int] =
    userOps.addPlayerShots(input)

  override def getByGameAndMaybeHole(gameId: GameId, hole: Option[Hole]): IO[List[UserShotInput]] =
    userOps.getByGameAndMaybeHole(gameId, hole)

  override def getHandicapHistory(playerId: PlayerId): IO[List[HandicapWithDate]] =
    userOps.getHandicapHistory(playerId)

  override def aggregateGameResult(gameId: GameId): IO[List[AggregateGameResult]] =
    userOps.aggregateGameResult(gameId)

}
