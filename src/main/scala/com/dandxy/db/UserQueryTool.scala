package com.dandxy.db

import java.sql.Timestamp

import cats.implicits._
import com.dandxy.model.user._
import doobie._
import doobie.implicits._

object UserQueryTool {

  import UserQueryToolSQL._

  def gdprDeleteByPlayerId(playerId: PlayerId): ConnectionIO[Int] =
    for {
      g <- purgeByPlayerId(playerId, "player.game").run
      c <- purgeByPlayerId(playerId, "player.club_data").run
      p <- purgeByPlayerId(playerId, "userSecurity.hashedpassword").run
      l <- purgeByPlayerId(playerId, "player.playerlookup").run
    } yield g + c + p + l

  def registerUser(registration: UserRegistration, hashPassword: Password, updateTime: Timestamp): ConnectionIO[PlayerId] =
    for {
      id <- addUser(registration, updateTime)
      _  <- addHashedPassword(registration.email, hashPassword, PlayerId(id)).run
    } yield PlayerId(id)

  def attemptLogin(email: UserEmail, hashPassword: Password): ConnectionIO[Option[PlayerId]] =
    checkLogin(email, hashPassword)

  // TODO: Improve this!
  def addClubData(input: List[GolfClubData]): ConnectionIO[List[Int]] =
    input.map(insertClubData).sequence

  //  def getUserGame: Unit = ???
  //
  //  def upsertUserGame: Unit = ???
  //
  //  def getUserShot: Unit = ???
  //
  //  def upsertUserShot: Unit = ???
  //
  //  def deleteUserForGDPR: Unit = ???
  //
  //  def upsertClubData: Unit = ???
  //
  //  def getClubData: Unit = ???

}
