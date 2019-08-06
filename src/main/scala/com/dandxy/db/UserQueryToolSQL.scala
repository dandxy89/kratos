package com.dandxy.db

import java.sql.Timestamp

import com.dandxy.model.user._
import doobie._
import doobie.implicits._

object UserQueryToolSQL {

  private[db] def purgeByPlayerId(playerId: PlayerId, tableName: String): Update0 =
    sql" DELETE FROM $tableName WHERE player_id = ${playerId.uuid}".update

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

  private[db] def insertClubData(clubData: GolfClubData): ConnectionIO[Int] =
    sql""" INSERT INTO player.club_data (player_id, club, typical_shape, typical_height,
         |                               manufacturer, typical_distance, distanceType)
         | VALUES (${clubData.playerId.uuid}, ${clubData.club.dbIndex}, ${clubData.typicalShape},
         |         ${clubData.typicalHeight}, ${clubData.manufacturer},
         |         ${clubData.typicalDistance}, ${clubData.distanceType})
       """.stripMargin.update.withUniqueGeneratedKeys[Int]("club_data_serial")

  private[db] def getClubData(clubSerialId: Int): Query0[GolfClubData] =
    sql""" SELECT player_id, club, typical_shape, typical_height, manufacturer, typical_distance, distanceType
         | FROM player.club_data
         | WHERE club_data_serial = $clubSerialId 
       """.stripMargin.query[GolfClubData]

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
