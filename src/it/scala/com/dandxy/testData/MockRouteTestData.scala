package com.dandxy.testData

import java.sql.Timestamp

import cats.effect.IO
import cats.implicits._
import com.dandxy.db.UserStore
import com.dandxy.golf.entity.Location
import com.dandxy.golf.entity.Score.Aggregate
import com.dandxy.golf.input.GolfInput.{ UserGameInput, UserShotInput }
import com.dandxy.golf.input.{ Distance, Handicap, HandicapWithDate, Points }
import com.dandxy.golf.pga.Statistic.PGAStatistic
import com.dandxy.model.player.PlayerId
import com.dandxy.model.user.Identifier.{ GameId, Hole }
import com.dandxy.model.user._
import com.dandxy.strokes.GolfResult

trait MockRouteTestData extends SimulationTestData {

  def mockAttemptLogin: (UserEmail, Password) => IO[Option[PlayerId]] =
    (e, _) =>
      e match {
        case UserEmail("test@gmail.com") => IO.pure(Option(PlayerId(1)))
        case _                           => None.pure[IO]
      }

  val mockStat: (Distance, Location) => IO[Option[PGAStatistic]] =
    (_, _) => IO.pure(Option(PGAStatistic(Distance(100), 1.1)))

  def dbResult(game: GameId): GolfResult =
    GolfResult(game, Aggregate(3), None, None, None, None, None, Points(2))

  val mockStore = new UserStore[IO] {

    def getByGameAndMaybeHole(gameId: GameId, hole: Option[Hole]): IO[List[UserShotInput]] =
      IO.pure(parThreeExample ++ parFourExample ++ parFiveExample)

    def addPlayerShots(input: List[UserShotInput]): IO[Int] =
      IO.pure(1989)

    def addResultByIdentifier(result: GolfResult, h: Option[Hole]): IO[Int] =
      IO.pure(1989)

    def getResultByIdentifier(game: GameId, h: Option[Hole]): IO[Option[GolfResult]] =
      IO.pure(Option(dbResult(game)))

    def getGameHandicap(game: GameId): IO[Option[Handicap]] =
      IO.pure(Option(Handicap(6.3)))

    // Not required for testing
    def gdprPurge(playerId: PlayerId): IO[Int]                                                                      = ???
    def registerUser(registration: UserRegistration, hashedPassword: Password, updateTime: Timestamp): IO[PlayerId] = ???
    def attemptLogin(email: UserEmail, rawPassword: Password): IO[Option[PlayerId]]                                 = ???
    def addClubData(playerId: PlayerId, input: List[GolfClubData]): IO[Int]                                         = ???
    def getUserClubs(playerId: PlayerId): IO[List[GolfClubData]]                                                    = ???
    def getAllPlayerGames(playerId: PlayerId): IO[List[UserGameInput]]                                              = ???
    def getPlayerGame(gameId: GameId): IO[Option[UserGameInput]]                                                    = ???
    def addPlayerGame(game: UserGameInput): IO[GameId]                                                              = ???
    def deletePlayerGame(gameId: GameId): IO[Int]                                                                   = ???
    def dropByHole(gameId: GameId, hole: Hole): IO[Int]                                                             = ???
    def getHandicapHistory(playerId: PlayerId): IO[List[HandicapWithDate]]                                          = ???
    def aggregateGameResult(gameId: GameId): IO[List[AggregateGameResult]]                                          = ???
  }
}
