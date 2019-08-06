package com.dandxy.db

import java.sql.Timestamp

import com.dandxy.db.sql.TableName._
import com.dandxy.model.user._
import doobie._

object UserQueryTool {

  import com.dandxy.db.sql.UserQueryToolSQL._

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

  def attemptLogin(email: UserEmail, hashPassword: Password): ConnectionIO[Option[PlayerId]] =
    checkLogin(email, hashPassword)

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
