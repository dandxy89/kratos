package com.dandxy.db

import java.sql.Timestamp

import com.dandxy.auth.PasswordAuth
import com.dandxy.config.AppModels.AuthSalt
import com.dandxy.db.sql.TableName._
import com.dandxy.model.golf.entity.Hole
import com.dandxy.model.golf.input.GolfInput.{ UserGameInput, UserShotInput }
import com.dandxy.model.golf.input.HandicapWithDate
import com.dandxy.model.user._
import doobie._

object UserQueryTool {

  import com.dandxy.db.sql.UserQueryToolSQL._

  private[db] final case class PlayerHash(playerId: PlayerId, password: Password)

  def gdprPurge(playerId: PlayerId): ConnectionIO[Int] =
    for {
      s <- purgeShotsByPlayerId(playerId, PlayerShot.name).run
      g <- purgeByPlayerId(playerId, PlayerGame.name).run
      c <- purgeByPlayerId(playerId, PlayerClubData.name).run
      p <- purgeByPlayerId(playerId, UserSecurity.name).run
      l <- purgeByPlayerId(playerId, PlayerLookup.name).run
    } yield g + c + p + l + s

  def registerUser(registration: UserRegistration, hashedPassword: Password, updateTime: Timestamp): ConnectionIO[PlayerId] =
    for {
      id <- addUser(registration, updateTime)
      _  <- addHashedPassword(registration.email, hashedPassword, PlayerId(id)).run
    } yield PlayerId(id)

  def attemptLogin(config: AuthSalt)(email: UserEmail, rawPassword: Password): ConnectionIO[Option[PlayerId]] =
    checkLogin(email).map {
      case None                    => None
      case Some(PlayerHash(p, ph)) => if (PasswordAuth.verify(rawPassword, ph.value, config.salt)) Some(p) else None
    }

  def addClubData(playerId: PlayerId, input: List[GolfClubData]): ConnectionIO[Int] =
    for {
      _ <- purgeByPlayerId(playerId, PlayerClubData.name).run
      c <- insertClubData(input)
    } yield c

  def getUserClubs(playerId: PlayerId): ConnectionIO[List[GolfClubData]] =
    fetchClubData(playerId)

  def getAllPlayerGames(playerId: PlayerId): ConnectionIO[List[UserGameInput]] =
    fetchAllPlayerGames(playerId)

  def getPlayerGame(gameId: GameId): ConnectionIO[Option[UserGameInput]] =
    fetchPlayerGame(gameId)

  def addPlayerGame(game: UserGameInput): ConnectionIO[GameId] =
    insertPlayerGame(game)

  def dropByHole(gameId: GameId, hole: Hole): Update0 =
    dropShotsByHole(gameId, hole)

  def addPlayerShots(input: List[UserShotInput]): ConnectionIO[Int] =
    insertPlayerShots(input)

  def getByGameAndMaybeHole(gameId: GameId, hole: Option[Hole]): ConnectionIO[List[UserShotInput]] =
    fetchPlayerShot(gameId, hole)

  def getHandicapHistory(playerId: PlayerId): ConnectionIO[List[HandicapWithDate]] =
    fetchHandicapHistory(playerId)

  def aggregateGameResult(gameId: GameId): ConnectionIO[List[AggregateGameResult]] =
    fetchAggregateGameResult(gameId)

}
