package com.dandxy.db.sql

import java.sql.Timestamp

import cats.implicits._
import com.dandxy.model.user._
import doobie._
import doobie.implicits._

object UserQueryToolSQL {

  private[db] def purgeByPlayerId(playerId: PlayerId, tableName: Fragment): Update0 =
    (fr" DELETE FROM " ++ tableName ++ fr" WHERE player_id = ${playerId.uuid}").update

  private[db] def purgeShotsByPlayerId(playerId: PlayerId, tableName: Fragment): Update0 =
    (fr" DELETE FROM "
      ++ tableName
      ++ fr" WHERE shot.game_id IN (SELECT g.game_id FROM player.game g WHERE g.player_id = ${playerId.uuid})").update

  private[db] def addUser(registration: UserRegistration, updateTime: Timestamp): ConnectionIO[Int] =
    sql""" INSERT INTO player.playerlookup (player_email, update_time, first_name, last_name)
         | VALUES (${registration.email}, $updateTime, ${registration.firstName}, ${registration.lastName})
         | """.stripMargin.update.withUniqueGeneratedKeys[Int]("player_id")

  private[db] def addHashedPassword(email: UserEmail, hashPassword: Password, playerId: PlayerId): Update0 =
    sql""" INSERT INTO userSecurity.hashedpassword (player_email, hashed_password, player_id)
         | VALUES ($email, $hashPassword, $playerId)
       """.stripMargin.update

  private[db] def checkLogin(email: UserEmail, hashPassword: Password): ConnectionIO[Option[PlayerId]] =
    sql""" SELECT player_id FROM userSecurity.hashedpassword
         | WHERE player_email = $email AND hashed_password = $hashPassword
       """.stripMargin.query[PlayerId].option

  private[db] def insertClubData(clubData: List[GolfClubData]): ConnectionIO[Int] = {
    val sql =
      """ INSERT INTO player.club_data (player_id, club, typical_shape, typical_height,
        |                               manufacturer, typical_distance, distanceType)
        | VALUES (?, ?, ?, ?, ?, ?, ?)
      """.stripMargin

    Update[GolfClubData](sql).updateMany(clubData)
  }

  private[db] def getClubData(playerId: PlayerId): ConnectionIO[List[GolfClubData]] =
    sql""" SELECT player_id, club, typical_shape, typical_height, manufacturer, typical_distance, distanceType
         | FROM player.club_data
         | WHERE player_id = $playerId
       """.stripMargin.query[GolfClubData].to[List]

  // CREATE TABLE player.game (
  //    game_id SERIAL PRIMARY KEY,
  //    player_id INTEGER REFERENCES player.playerlookup,
  //    course VARCHAR(200) NOT NULL,
  //    game_start_time TIMESTAMP NOT NULL,
  //    handicap NUMERIC(6, 1) NOT NULL,
  //    ball_used VARCHAR(200),
  //    green_speed VARCHAR(200),
  //    temperature VARCHAR(200),
  //    wind_speed VARCHAR(200)
  //);

  private[db] def addPlayerGame() = ???

  private[db] def getPlayerGame() = ???

  //CREATE TABLE player.shot (
  //    shot_serial SERIAL PRIMARY KEY,
  //    game_id INTEGER REFERENCES player.game,
  //    hole INTEGER NOT NULL,
  //    shot INTEGER NOT NULL,
  //    par INTEGER NOT NULL,
  //    distance INTEGER NOT NULL,
  //    ball_location INTEGER NOT NULL,
  //    club VARCHAR(200) NOT NULL,
  //    strokes_gained NUMERIC(6, 3) NOT NULL,
  //    orientation VARCHAR(200),
  //    shot_shape VARCHAR(200),
  //    shot_height VARCHAR(200),
  //    stroke_index VARCHAR(200)
  //);

  private[db] def addPlayerShot() = ???

  private[db] def getPlayerShot() = ???

}
