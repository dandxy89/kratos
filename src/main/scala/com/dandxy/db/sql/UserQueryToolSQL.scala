package com.dandxy.db.sql

import java.sql.Timestamp

import cats.implicits._
import com.dandxy.db.UserQueryTool.PlayerHash
import com.dandxy.model.golf.entity.Hole
import com.dandxy.model.golf.input.GolfInput.{ UserGameInput, UserShotInput }
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

  private[db] def checkLogin(email: UserEmail): ConnectionIO[Option[PlayerHash]] =
    sql""" SELECT player_id, hashed_password
         | FROM userSecurity.hashedpassword
         | WHERE player_email = $email
       """.stripMargin.query[PlayerHash].option

  private[db] def insertClubData(clubData: List[GolfClubData]): ConnectionIO[Int] = {
    val sql =
      """ INSERT INTO player.club_data (player_id, club, typical_shape, typical_height,
        |                               manufacturer, typical_distance, distanceType)
        | VALUES (?, ?, ?, ?, ?, ?, ?)
      """.stripMargin

    Update[GolfClubData](sql).updateMany(clubData)
  }

  private[db] def fetchClubData(playerId: PlayerId): ConnectionIO[List[GolfClubData]] =
    sql""" SELECT player_id, club, typical_shape, typical_height, manufacturer, typical_distance, distanceType
         | FROM player.club_data
         | WHERE player_id = $playerId
       """.stripMargin.query[GolfClubData].to[List]

  private[db] def insertPlayerGame(game: UserGameInput): Update0 =
    sql""" INSERT INTO player.game (player_id, course, game_start_time, handicap, ball_used, 
         |                          green_speed, temperature, wind_speed)
         | VALUES (${game.playerId}, ${game.courseName}, ${game.gameStartTime}, ${game.handicap},
         |         ${game.ballUsed}, ${game.greenSpeed}, ${game.temperature}, ${game.windSpeed})
       """.stripMargin.update

  private[db] def fetchPlayerGame(gameId: GameId): Query0[UserGameInput] =
    sql""" SELECT player_id, game_start_time, handicap, ball_used, green_speed, temperature, wind_speed, game_id
         | FROM player.game
         | WHERE game_id = $gameId
       """.stripMargin.query[UserGameInput]

  private[db] def fetchAllPlayerGames(playerId: PlayerId): ConnectionIO[List[UserGameInput]] =
    sql""" SELECT player_id, game_start_time, handicap, ball_used, green_speed, temperature, wind_speed, game_id
         | FROM player.game
         | WHERE player_id = $playerId
       """.stripMargin.query[UserGameInput].to[List]

  private[db] def dropShotsByHole(gameId: GameId, hole: Hole): Update0 =
    sql""" DELETE FROM player.shot WHERE game_id = $gameId AND hole = $hole """.update

  private[db] def insertPlayerShots(input: List[UserShotInput]): ConnectionIO[Int] = {
    val sql =
      """ INSERT INTO player.shot (game_id, hole, shot, par, distance, ball_location, club,
        |                          strokes_gained, stroke_index, orientation, shot_shape, shot_height)
        | VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
      """.stripMargin

    Update[UserShotInput](sql).updateMany(input)
  }

  private[this] def holeClause(hole: Option[Hole]): Fragment = hole match {
    case Some(h) => fr" AND hole = $h"
    case None    => fr""
  }

  private[db] def fetchPlayerShot(gameId: GameId, hole: Option[Hole]): ConnectionIO[List[UserShotInput]] =
    (fr"""SELECT games_id, hole, shot, par, distance, ball_location, 
         |       club, strokes_gained, stroke_index, orientation, shot_shape, shot_height
         | FROM player.shot WHERE game_id = $gameId""".stripMargin ++ holeClause(hole))
      .query[UserShotInput]
      .to[List]
}
