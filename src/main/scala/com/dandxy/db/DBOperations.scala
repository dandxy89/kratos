package com.dandxy.db

import java.sql.Timestamp

import com.dandxy.model.golf.entity.PGAStatistics
import com.dandxy.model.golf.input.GolfInput.{UserGameInput, UserShotInput}
import com.dandxy.model.golf.input.{Distance, GolfResult, HandicapWithDate}
import com.dandxy.model.golf.pga.Statistic.PGAStatistic
import com.dandxy.model.player.PlayerId
import com.dandxy.model.user.Identifier.{GameId, Hole}
import com.dandxy.model.user._

import scala.language.higherKinds

class DBOperations[F[_]](pgaOps: PGAPostgresQueryTool[F], userOps: UserPostgresQueryTool[F]) extends PGAStore[F] with UserStore[F] {

  override def getStatistic(distance: Distance, stat: PGAStatistics): F[Option[PGAStatistic]] =
    pgaOps.getStatistic(distance, stat)

  override def gdprPurge(playerId: PlayerId): F[Int] =
    userOps.gdprPurge(playerId)

  override def registerUser(registration: UserRegistration, hashedPassword: Password, updateTime: Timestamp): F[PlayerId] =
    userOps.registerUser(registration, hashedPassword, updateTime)

  override def attemptLogin(email: UserEmail, rawPassword: Password): F[Option[PlayerId]] =
    userOps.attemptLogin(email, rawPassword)

  override def addClubData(playerId: PlayerId, input: List[GolfClubData]): F[Int] =
    userOps.addClubData(playerId, input)

  override def getUserClubs(playerId: PlayerId): F[List[GolfClubData]] =
    userOps.getUserClubs(playerId)

  override def getAllPlayerGames(playerId: PlayerId): F[List[UserGameInput]] =
    userOps.getAllPlayerGames(playerId)

  override def getPlayerGame(gameId: GameId): F[Option[UserGameInput]] =
    userOps.getPlayerGame(gameId)

  override def addPlayerGame(game: UserGameInput): F[GameId] =
    userOps.addPlayerGame(game)

  override def dropByHole(gameId: GameId, hole: Hole): F[Int] =
    userOps.dropByHole(gameId, hole)

  override def addPlayerShots(input: List[UserShotInput]): F[Int] =
    userOps.addPlayerShots(input)

  override def getByGameAndMaybeHole(gameId: GameId, hole: Option[Hole]): F[List[UserShotInput]] =
    userOps.getByGameAndMaybeHole(gameId, hole)

  override def getHandicapHistory(playerId: PlayerId): F[List[HandicapWithDate]] =
    userOps.getHandicapHistory(playerId)

  override def aggregateGameResult(gameId: GameId): F[List[AggregateGameResult]] =
    userOps.aggregateGameResult(gameId)

  override def addResultByIdentifier(result: GolfResult): F[Int] =
    userOps.addResultByIdentifier(result)

  override def getResultByIdentifier(id: Identifier): F[List[GolfResult]] =
    userOps.getResultByIdentifier(id)
}
