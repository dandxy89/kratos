package com.dandxy.db

import java.sql.Timestamp

import com.dandxy.auth.PasswordAuth
import com.dandxy.config.AppModels.AuthSalt
import com.dandxy.db.sql.TableName._
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

  def registerUser(registration: UserRegistration, hashPassword: Password, updateTime: Timestamp): ConnectionIO[PlayerId] =
    for {
      id <- addUser(registration, updateTime)
      _  <- addHashedPassword(registration.email, hashPassword, PlayerId(id)).run
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
    getClubData(playerId)

  //  def upsertUserGame: Unit = ???
  //  def getUserShot: Unit = ???
  //  def upsertUserShot: Unit = ???

}
