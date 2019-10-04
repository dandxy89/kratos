package com.dandxy.testData

import java.sql.Timestamp

import cats.effect.IO
import cats.implicits._
import com.dandxy.db.UserStore
import com.dandxy.golf.entity.GolfClub.FourIron
import com.dandxy.golf.entity.{ Location, Par }
import com.dandxy.golf.entity.Score.Aggregate
import com.dandxy.golf.input.DistanceMeasurement.Feet
import com.dandxy.golf.input.GolfInput.{ UserGameInput, UserShotInput }
import com.dandxy.golf.input.{ Distance, Handicap, HandicapWithDate, Points }
import com.dandxy.golf.pga.Statistic.PGAStatistic
import com.dandxy.model.player.PlayerId
import com.dandxy.model.user.Identifier.{ GameId, Hole }
import com.dandxy.model.user._
import com.dandxy.strokes.GolfResult
import org.http4s.{ Header, Method, Request, Response, Uri }
import org.scalatest.{ Assertion, FlatSpec, Matchers }

trait MockRouteTestData extends SimulationTestData with FlatSpec with Matchers {

  def validateResult(resp: Option[Response[IO]], cond: Response[IO] => Assertion): Unit =
    resp match {
      case None        => fail("Did not match on route correctly")
      case Some(value) => cond(value)
    }

  def makeRequest(token: String)(method: Method, routeString: String, validBody: String): Request[IO] =
    Request[IO](method, Uri.unsafeFromString(routeString))
      .withEntity(validBody)
      .withHeaders(
        Header("Accept", "application/json"),
        Header("Content-Type", "application/json"),
        Header("Authorization", s"Bearer $token")
      )

  def mockAttemptLogin: (UserEmail, Password) => IO[Option[PlayerId]] =
    (e, _) =>
      e match {
        case UserEmail("test@gmail.com") => IO.pure(Option(PlayerId(1)))
        case _                           => None.pure[IO]
      }

  val mockStat: (Distance, Location) => IO[Option[PGAStatistic]] =
    (_, _) => IO.pure(Option(PGAStatistic(Distance(100), 1.1)))

  def dbResult(game: GameId): GolfResult = GolfResult(game, Aggregate(3), None, None, None, None, None, Points(2))

  val mockStore: UserStore[IO] = new UserStore[IO] {

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

    def addClubData(playerId: PlayerId, input: List[GolfClubData]): IO[Int] =
      IO.pure(2)

    def getUserClubs(playerId: PlayerId): IO[List[GolfClubData]] =
      IO.pure(List(GolfClubData(PlayerId(3), FourIron, None, None, None, Distance(100), Feet)))

    def getAllPlayerGames(playerId: PlayerId): IO[List[UserGameInput]] =
      IO.pure(
        List(
          UserGameInput(PlayerId(3), new Timestamp(1231233123), "Test Course", Handicap(5), None, None, None, None, None)
        )
      )

    def getPlayerGame(gameId: GameId): IO[Option[UserGameInput]] = gameId match {
      case GameId(id) if id == 3 => IO.pure(None)
      case _ =>
        IO.pure(
          Option(UserGameInput(PlayerId(3), new Timestamp(1231233123), "Test Course", Handicap(5), None, None, None, None, None))
        )
    }

    def deletePlayerGame(gameId: GameId): IO[Int] = IO.pure(1)

    def getHandicapHistory(playerId: PlayerId): IO[List[HandicapWithDate]] =
      IO.pure(
        List(HandicapWithDate(Handicap(3), new Timestamp(1231231221)), HandicapWithDate(Handicap(3.1), new Timestamp(1231231231)))
      )

    def addPlayerGame(game: UserGameInput): IO[GameId] = IO.pure(GameId(10))

    def aggregateGameResult(gameId: GameId): IO[List[AggregateGameResult]] =
      IO.pure(
        List(
          AggregateGameResult(gameId, Hole(3), Distance(340), Par.ParFour, 1, 5, None)
        )
      )

    // Not required for testing
    def gdprPurge(playerId: PlayerId): IO[Int]                                                                      = ???
    def registerUser(registration: UserRegistration, hashedPassword: Password, updateTime: Timestamp): IO[PlayerId] = ???
    def attemptLogin(email: UserEmail, rawPassword: Password): IO[Option[PlayerId]]                                 = ???
    def dropByHole(gameId: GameId, hole: Hole): IO[Int]                                                             = ???

  }

  val addClubBody: String =
    """
      |[
      |  {
      |    "playerId":3,
      |    "club":1,
      |    "typicalShape":3,
      |    "typicalHeight":3,
      |    "manufacturer":2,
      |    "typicalDistance":278,
      |    "distanceType":2
      |  },
      |  {
      |    "playerId":3,
      |    "club":2,
      |    "typicalShape":2,
      |    "typicalHeight":1,
      |    "manufacturer":4,
      |    "typicalDistance":234,
      |    "distanceType":2
      |  }
      |]
      |""".stripMargin

  val addGame: String =
    """
      |{
      |    "playerId": 3,
      |    "gameStartTime": 1568241842,
      |    "courseName": "Burhill Golf Club - New Course",
      |    "handicap": 6.3,
      |    "ballUsed": "Truesoft",
      |    "temperature": 18.2
      |}
      |""".stripMargin

}
