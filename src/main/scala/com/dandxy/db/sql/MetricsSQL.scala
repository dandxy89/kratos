package com.dandxy.db.sql

import com.dandxy.db.sql.SQLOrder.Descending
import com.dandxy.golf.input.{ Distance, Strokes }
import com.dandxy.model.stats._
import com.dandxy.model.user.Identifier.{ GameId, Hole }
import doobie._
import doobie.implicits._

object MetricsSQL {

  import PostgresInstances._

  private[db] def getStrokesGainedByClub(gameId: GameId): ConnectionIO[List[StokesGainedByClub]] =
    sql""" SELECT club, MIN(strokes_gained), AVG(strokes_gained), MAX(strokes_gained)
         | FROM player.shot
         | WHERE game_id = $gameId
         | GROUP BY club """.stripMargin.query[StokesGainedByClub].to[List]

  private[db] def getGameGreensInRegulation(gameId: GameId): ConnectionIO[Option[InRegulation]] =
    sql""" SELECT SUM( CASE WHEN ball_location = 6 THEN 1 ELSE 0 END ) AS GreenCount,
         |        COUNT(ball_location) AS TotalHolesPlayed
         | FROM player.shot
         | WHERE game_id = $gameId
         |       AND ( 
         |              (par = 3 and shot <= 2) 
         |           OR 
         |              (par = 4 and shot <= 3) 
         |           OR 
         |              (par = 5 and shot <= 4) 
         | )
         | GROUP BY game_id """.stripMargin.query[InRegulation].option

  private[db] def getFairwaysInRegulation(gameId: GameId): ConnectionIO[Option[InRegulation]] =
    sql""" SELECT SUM( CASE WHEN ball_location = 2 THEN 1 ELSE 0 END ) AS GreenCount,
         |        COUNT(ball_location) AS TotalHolesPlayed
         | FROM player.shot 
         | WHERE game_id = $gameId
         |       AND ( 
         |              (par = 3 and shot <= 2) 
         |          OR 
         |              (par = 4 and shot <= 3) 
         |          OR 
         |              (par = 5 and shot <= 4) 
         | )
         | GROUP BY game_id """.stripMargin.query[InRegulation].option

  private[db] def getAverageDistanceByClub(gameId: GameId): ConnectionIO[List[ClubDistance]] =
    sql""" SELECT  club, AVG(distance)
         | FROM player.shot
         | WHERE game_id = $gameId
         | GROUP BY club
         | ORDER BY club ASC """.stripMargin.query[ClubDistance].to[List]

  private[db] def getPuttsPerRound(gameId: GameId): ConnectionIO[Option[Strokes]] =
    sql""" SELECT COUNT(*)
         | FROM player.shot
         | WHERE game_id = $gameId AND ball_location = 6 """.stripMargin.query[Strokes].option

  private[db] def getPenaltiesPerRound(gameId: GameId): ConnectionIO[Option[Strokes]] =
    sql""" SELECT COUNT(*)
         | FROM "player"."shot"
         | WHERE game_id = $gameId AND ball_location > 6 """.stripMargin.query[Strokes].option

  private[db] def getPuttsToHoleFromDistance(gameId: GameId, size: Int, max: Int): ConnectionIO[List[PuttsByDistanceToHole]] =
    sql""" SELECT width_bucket(s.distance, 0, $max, $max / $size) * $size AS PuttDistanceWindow
         |        , COUNT(*) AS CountStrokes
         | FROM player.shot s
         | INNER JOIN (
         |    SELECT hole, MAX(shot) AS shot
         |    FROM player.shot
         |    WHERE game_id = $gameId
         |    GROUP BY hole
         | ) j ON j.hole = s.hole AND s.shot = j.shot
         | WHERE s.ball_location = 6 AND game_id = $gameId
         | GROUP BY PuttDistanceWindow """.stripMargin.query[PuttsByDistanceToHole].to[List]

  private[db] def getStandardScores(gameId: GameId): ConnectionIO[List[StandardScores]] =
    sql""" SELECT
         |    SUM(CASE WHEN s.shot = s.par - 2 THEN 1 ELSE 0 END) AS Eagle
         |    , SUM(CASE WHEN s.shot = s.par - 1 THEN 1 ELSE 0 END) AS Birdie
         |    , SUM(CASE WHEN s.shot = s.par THEN 1 ELSE 0 END) AS Par
         |    , SUM(CASE WHEN s.shot = s.par + 1 THEN 1 ELSE 0 END) AS Bogey
         |    , SUM(CASE WHEN s.shot = s.par + 2 THEN 1 ELSE 0 END) AS DoubleBogey
         |    , SUM(CASE WHEN s.shot = s.par + 3 THEN 1 ELSE 0 END) AS TripleBogey
         |    , SUM(CASE WHEN s.shot > s.par + 3 THEN 1 ELSE 0 END) AS OtherBogey
         | FROM player.shot s
         | INNER JOIN (
         |    SELECT hole, MAX(shot) AS shot
         |    FROM player.shot
         |    WHERE game_id = $gameId
         |    GROUP BY hole
         | ) j ON j.hole = s.hole AND s.shot = j.shot
         | WHERE game_id = $gameId
         | GROUP BY game_id """.stripMargin.query[StandardScores].to[List]

  private[db] def getHoledPuttDistance(gameId: GameId): Query0[Distance] =
    sql""" SELECT SUM(s.distance) AS TotalLengthOfPutts
         | FROM player.shot s
         | INNER JOIN (
         |    SELECT hole, MAX(shot) AS shot
         |    FROM player.shot
         |    WHERE game_id = $gameId
         |    GROUP BY hole
         | ) j ON j.hole = s.hole AND s.shot = j.shot
         | WHERE s.ball_location = 6 AND game_id = $gameId """.stripMargin.query[Distance]

  private[db] def getStrokesGainedWithoutNShots(gameId: GameId, n: Int): Query0[Strokes] =
    sql""" SELECT SUM(strokes_gained)
         | FROM "player"."shot"
         | WHERE shot_serial NOT IN (
         |    SELECT  shot_serial
         |    FROM "player"."shot"
         |    WHERE game_id = $gameId
         |    ORDER BY strokes_gained DESC
         |    LIMIT $n
         | ) AND game_id = $gameId
         | GROUP BY game_id """.stripMargin.query[Strokes]

  private[db] def getAverageDriveDistance(gameId: GameId): ConnectionIO[List[AverageDriveDistance]] =
    sql""" SELECT club, AVG((m.distance - n.distance)) AS AveragePuttDistance
         | FROM player.shot m
         | LEFT JOIN (
         |    SELECT distance, hole
         |    FROM player.shot
         |    WHERE game_id = $gameId AND shot = 2
         | ) n ON m.hole = n.hole
         | WHERE m.game_id = $gameId AND shot = 1 AND (m.distance - n.distance) > 0 AND m.par > 3
         | GROUP BY club """.stripMargin.query[AverageDriveDistance].to[List]

  private[db] def getXStrokesGainedShots(gameId: GameId, n: Int, best: SQLOrder): ConnectionIO[List[StrokesGainedResults]] =
    best match {
      case Descending =>
        sql""" SELECT hole, shot, par, distance, ball_location, club, strokes_gained, stroke_index
             | FROM player.shot
             | WHERE game_id = $gameId
             | ORDER BY strokes_gained DESC
             | LIMIT $n """.stripMargin.query[StrokesGainedResults].to[List]
      case _ =>
        sql""" SELECT hole, shot, par, distance, ball_location, club, strokes_gained, stroke_index
             | FROM player.shot
             | WHERE game_id = $gameId AND strokes_gained IS NOT NULL
             | ORDER BY strokes_gained ASC
             | LIMIT $n """.stripMargin.query[StrokesGainedResults].to[List]
    }

  private[db] def getHolesGIR(gameId: GameId): ConnectionIO[List[Hole]] =
    sql""" SELECT hole
         | FROM  player.shot
         | WHERE game_id = $gameId
         | AND (
         |    (shot <= 2 AND par = 3 AND ball_location = 6)
         |    OR
         |    (shot <= 3 AND par = 3 AND ball_location = 6)
         |    OR
         |    (shot <= 4 AND par = 5 AND ball_location = 6)
         | ) """.stripMargin.query[Hole].to[List]

  private[db] def getHolesNotGIR(gameId: GameId): ConnectionIO[List[Hole]] =
    sql""" SELECT DISTINCT hole
         | FROM player.shot
         | WHERE game_id = $gameId
         | AND hole NOT IN (
         |    SELECT hole
         |    FROM player.shot
         |    WHERE game_id = $gameId
         |    AND (
         |        (shot <= 2 AND par = 3 AND ball_location = 6)
         |        OR
         |        (shot <= 3 AND par = 4 AND ball_location = 6)
         |        OR
         |        (shot <= 4 AND par = 5 AND ball_location = 6)
         |    )
         | )
         |""".stripMargin.query[Hole].to[List]

  private[db] def getAveragePuttsWhenGIR(gameId: GameId): Query0[Strokes] =
    sql""" SELECT CAST(CALC.NUM AS FLOAT) / CAST(CALC.DEMON AS FLOAT)
         | FROM (
         |    SELECT
         |    (
         |        SELECT COUNT(*)
         |        FROM player.shot
         |        WHERE game_id = $gameId
         |        AND ball_location = 6
         |        AND hole IN (
         |            SELECT hole
         |            FROM player.shot
         |            WHERE game_id = $gameId
         |            AND (
         |                (shot <= 2 AND par = 3 AND ball_location = 6)
         |                OR
         |                (shot <= 3 AND par = 4 AND ball_location = 6)
         |                OR
         |                (shot <= 4 AND par = 5 AND ball_location = 6)
         |            )
         |        )
         |    ) AS NUM,
         |    (
         |        SELECT COUNT(DISTINCT hole)
         |        FROM player.shot
         |        WHERE game_id = $gameId
         |        AND (
         |            (shot <= 2 AND par = 3 AND ball_location = 6)
         |            OR
         |            (shot <= 3 AND par = 4 AND ball_location = 6)
         |            OR
         |            (shot <= 4 AND par = 5 AND ball_location = 6)
         |        )
         |
         |    ) AS DEMON
         | ) CALC """.stripMargin.query[Strokes]

  private[db] def getAveragePuttsWhenNotGIR(gameId: GameId): Query0[Strokes] =
    sql""" SELECT CAST(CALC.NUM AS FLOAT) / CAST(CALC.DEMON AS FLOAT)
         | FROM (
         |    SELECT
         |    (
         |        SELECT COUNT(*)
         |        FROM player.shot
         |        WHERE game_id = $gameId
         |        AND ball_location = 6
         |        AND hole NOT IN (
         |            SELECT hole
         |            FROM player.shot
         |            WHERE game_id = $gameId
         |            AND (
         |                (shot <= 2 AND par = 3 AND ball_location = 6)
         |                OR
         |                (shot <= 3 AND par = 4 AND ball_location = 6)
         |                OR
         |                (shot <= 4 AND par = 5 AND ball_location = 6)
         |            )
         |        )
         |    ) AS NUM,
         |    (
         |        SELECT COUNT(DISTINCT hole)
         |        FROM player.shot
         |        WHERE game_id = $gameId
         |        AND hole NOT IN (
         |            SELECT hole
         |            FROM player.shot
         |            WHERE game_id = $gameId
         |            AND (
         |                (shot <= 2 AND par = 3 AND ball_location = 6)
         |                OR
         |                (shot <= 3 AND par = 4 AND ball_location = 6)
         |                OR
         |                (shot <= 4 AND par = 5 AND ball_location = 6)
         |            )
         |        )
         |    ) AS DEMON
         | ) CALC """.stripMargin.query[Strokes]

  private[db] def getAverageFirstPuttWhenGIR(gameId: GameId): ConnectionIO[Option[Distance]] =
    sql""" SELECT AVG(s.distance)
         | FROM player.shot s
         | INNER JOIN (
         |    SELECT hole, MIN(shot) AS shot
         |    FROM player.shot
         |    WHERE game_id = $gameId 
         |    AND ball_location = 6
         |    AND hole IN (
         |        SELECT hole
         |        FROM player.shot
         |        WHERE game_id = $gameId
         |        AND (
         |            (shot <= 2 AND par = 3 AND ball_location = 6)
         |            OR
         |            (shot <= 3 AND par = 4 AND ball_location = 6)
         |            OR
         |            (shot <= 3 AND par = 4 AND ball_location = 6)
         |        )
         |    )
         |    GROUP BY hole
         | ) j ON j.hole = s.hole AND j.shot = s.shot
         | WHERE game_id = $gameId """.stripMargin.query[Distance].option

  private[db] def getAverageFirstPuttWhenNotGIR(gameId: GameId): ConnectionIO[Option[Distance]] =
    sql""" SELECT AVG(s.distance)
         | FROM player.shot s
         | INNER JOIN (
         |    SELECT hole, MIN(shot) AS shot
         |    FROM player.shot
         |    WHERE game_id = $gameId
         |    AND ball_location = 6
         |    AND hole NOT IN (
         |        SELECT hole
         |        FROM player.shot
         |        WHERE game_id = $gameId
         |        AND (
         |            (shot <= 2 AND par = 3 AND ball_location = 6)
         |            OR
         |            (shot <= 3 AND par = 4 AND ball_location = 6)
         |            OR
         |            (shot <= 3 AND par = 4 AND ball_location = 6)
         |        )
         |    )
         |    GROUP BY hole
         | ) j ON j.hole = s.hole AND j.shot = s.shot
         | WHERE game_id = $gameId """.stripMargin.query[Distance].option

}
