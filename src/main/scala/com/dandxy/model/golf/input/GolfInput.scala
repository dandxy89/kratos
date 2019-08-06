package com.dandxy.model.golf.input

import java.time.LocalDate

import com.dandxy.model.golf.entity._
import com.dandxy.model.user.PlayerId

sealed trait GolfInput

object GolfInput {

  final case class UserHoleInput(
    playerId: PlayerId,
    puttingMetric: DistanceMeasurement,
    hole: Hole,
    par: Par,
    golfInput: List[ShotInput],
    handicap: Int,
    ballUsed: Option[String],
    greenSpeed: Option[Double],
    temperature: Option[Temperature],
    windSpeed: Option[WindSpeed]
  ) extends GolfInput

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

  final case class ShotInput(
    distance: Distance,
    shot: Int,
    location: Location,
    orientation: Option[Orientation],
    club: GolfClub,
    shotShape: Option[ShotShape],
    shotHeight: Option[ShotHeight],
    strokeIndex: Int
  ) extends GolfInput

  // CREATE TABLE player.shot (
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

  final case class UserCourseInput(courseName: String, holeInput: List[UserHoleInput], gameStartTime: LocalDate) extends GolfInput

}
