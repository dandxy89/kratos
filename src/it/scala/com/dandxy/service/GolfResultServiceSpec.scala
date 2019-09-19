package com.dandxy.service

import cats.effect._
import org.scalatest.{ FlatSpec, Matchers }
import com.dandxy.db.UserStore
import com.dandxy.model.player.PlayerId
import com.dandxy.model.user.UserRegistration
import com.dandxy.model.user.Password
import java.sql.Timestamp
import com.dandxy.golf.input.GolfInput.UserGameInput
import com.dandxy.model.user.UserEmail
import com.dandxy.model.user.Identifier.GameId
import com.dandxy.model.user.GolfClubData
import com.dandxy.golf.input.GolfInput.UserShotInput
import com.dandxy.golf.input.HandicapWithDate
import com.dandxy.model.user.Identifier.Hole
import com.dandxy.model.user.AggregateGameResult
import com.dandxy.strokes.GolfResult
import com.dandxy.golf.input.Distance
import com.dandxy.golf.entity.Location
import com.dandxy.golf.pga.Statistic.PGAStatistic
import com.dandxy.golf.input.Handicap
import GolfResultService._
import com.dandxy.golf.input.Points
import com.dandxy.golf.entity.Score.Aggregate
import com.dandxy.testData.SimulationTestData
import com.dandxy.golf.entity.Score.ParredHole
import com.dandxy.golf.input.Strokes

import scala.concurrent.ExecutionContext

class GolfResultServiceSpec extends FlatSpec with Matchers with SimulationTestData {

  behavior of "GolfResultServiceSpec"

  implicit private val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

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

  it should "processHoleResult" in {
    val res1 = processHoleResult(mockStore, mockStat)(GameId(123), Option(Hole(1)), true).unsafeRunSync()
    val res2 = processHoleResult(mockStore, mockStat)(GameId(124), Option(Hole(1)), false).unsafeRunSync()

    res1 match {
      case None => fail()
      case Some(value) =>
        value shouldBe GolfResult(
          GameId(1),
          ParredHole,
          Some(Strokes(-1.9)),
          Some(Strokes(0.0)),
          Some(Strokes(-1.0)),
          None,
          Some(Strokes(-0.9)),
          Points(3)
        )
    }

    res2 match {
      case None        => fail()
      case Some(value) => value shouldBe GolfResult(GameId(124), Aggregate(3), None, None, None, None, None, Points(2))
    }
  }

  it should "processGolfResult" in {
    val res1 = processGolfResult(mockStore, mockStat)(GameId(125), true).unsafeRunSync()
    val res2 = processGolfResult(mockStore, mockStat)(GameId(126), false).unsafeRunSync()

    res1 shouldBe GolfResult(
      GameId(125),
      Aggregate(0),
      Some(Strokes(-8.7)),
      Some(Strokes(-2.0)),
      Some(Strokes(-4.0)),
      None,
      Some(Strokes(-2.7)),
      Points(9)
    )
    res2 shouldBe GolfResult(GameId(126), Aggregate(3), None, None, None, None, None, Points(2))
  }
}
