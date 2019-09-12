package com.dandxy.db

import java.sql.Timestamp

import cats.effect.{ IO, Timer }
import cats.implicits._
import com.dandxy.auth.Salt
import com.dandxy.config.{ AuthSalt, DatabaseConfig }
import com.dandxy.golf.entity.GolfClub.{ Driver, FourIron, Putter }
import com.dandxy.golf.entity.Location.{ OnTheGreen, TeeBox }
import com.dandxy.golf.entity.Manufacturer.Miura
import com.dandxy.golf.entity.Orientation.{ LongLeft, MiddleLeft }
import com.dandxy.golf.entity.Par.ParThree
import com.dandxy.golf.entity.Score.Birdie
import com.dandxy.golf.input.{ Distance, Handicap, Points, Strokes, WindSpeed }
import com.dandxy.golf.input.DistanceMeasurement.Yards
import com.dandxy.golf.input.GolfInput.{ UserGameInput, UserShotInput }
import com.dandxy.golf.input.ShotHeight.Low
import com.dandxy.golf.input.ShotShape.Straight
import com.dandxy.model.player.PlayerId
import com.dandxy.model.user.Identifier.{ GameId, Hole }
import com.dandxy.model.user._
import com.dandxy.strokes.GolfResult
import com.dandxy.util.{ Helpers, PostgresDockerService }
import org.scalatest.concurrent.Eventually
import org.scalatest.{ BeforeAndAfterAll, FlatSpec, Matchers }
import cats.implicits._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class UserQueryToolSpec extends FlatSpec with Matchers with Eventually with BeforeAndAfterAll {

  val service = new PostgresDockerService(5446)

  eventually(service.isPostgresRunning shouldBe true)

  implicit val t: Timer[IO] = IO.timer(ExecutionContext.global)

  override def beforeAll(): Unit =
    (DatabaseConfig
      .initializeDb[IO](service.config) *> IO.sleep(3.seconds)).unsafeRunSync()

  // Players
  val userOne   = PlayerId(1)
  val userThree = PlayerId(3)

  // Registration
  val testTS    = new Timestamp(1565127644)
  val testEmail = UserEmail("it@golf.com")
  val reg       = UserRegistration(testEmail, "it", "isFun")
  val pass      = Password("testPassword")

  // Clubs
  val testGolfClubs  = List(GolfClubData(userOne, Driver, Some(Straight), Some(Low), Some(Miura), Distance(200.0), Yards))
  val testGolfClubs2 = List(GolfClubData(PlayerId(3), Driver, Some(Straight), Some(Low), Some(Miura), Distance(200.0), Yards))

  // Auth
  val fakeEmail: UserEmail = UserEmail("fake@hacker.com")
  val authSalt: AuthSalt   = AuthSalt(Some(Salt("testingSalt")))

  // Query Tool
  val queryTool = UserPostgresQueryInterpreter(service.postgresTransactor, authSalt)

  "UserQueryTool" should "comply with GDPR regulations" in {
    queryTool.gdprPurge(PlayerId(2)).unsafeRunSync() shouldBe 7
  }

  it should "register a user correctly" in {
    queryTool.registerUser(reg, pass, testTS).unsafeRunSync() shouldBe userThree
  }

  it should "login a user correctly" in {
    queryTool.attemptLogin(testEmail, pass).unsafeRunSync() shouldBe Some(userThree)
  }

  it should "return None when a user doesn't exist" in {
    queryTool.attemptLogin(fakeEmail, pass).unsafeRunSync() shouldBe None
  }

  it should "add club data correctly" in {
    queryTool.addClubData(userThree, testGolfClubs2).unsafeRunSync() shouldBe 1
  }

  it should "get club data into a list correctly" in {
    queryTool.getUserClubs(userOne).unsafeRunSync() shouldBe testGolfClubs
    queryTool.getUserClubs(userThree).unsafeRunSync() shouldBe testGolfClubs2
  }

  it should "get all of the players games in the database" in {
    queryTool
      .getAllPlayerGames(userOne)
      .unsafeRunSync()
      .map(_.gameId) shouldBe List(Some(GameId(1)), Some(GameId(3)))
  }

  it should "get one of the players games in the database" in {
    queryTool
      .getPlayerGame(GameId(1))
      .unsafeRunSync()
      .flatMap(_.gameId) shouldBe Some(GameId(1))
  }

  it should "successfully add a new game to the database" in {
    val gameTest =
      UserGameInput(
        PlayerId(1),
        new Timestamp(System.currentTimeMillis()),
        "IT Test TPC",
        Handicap(1.1),
        None,
        None,
        None,
        Some(WindSpeed.LightWind),
        None
      )
    queryTool.addPlayerGame(gameTest).unsafeRunSync() shouldBe GameId(4)
  }

  it should "fetch shots by Game - shouldBe Nil" in {
    queryTool.getByGameAndMaybeHole(GameId(4), None).unsafeRunSync() shouldBe Nil
  }

  it should "add Player Shots" in {
    val parThreeExample: List[UserShotInput] = List(
      UserShotInput(GameId(1), Hole(8), 1, ParThree, Distance(210), TeeBox, FourIron, None, 1, Option(MiddleLeft), None, None, None),
      UserShotInput(GameId(1), Hole(8), 2, ParThree, Distance(10), OnTheGreen, Putter, None, 1, Option(LongLeft), None, None, None),
      UserShotInput(GameId(1), Hole(8), 3, ParThree, Distance(2), OnTheGreen, Putter, None, 1, Option(MiddleLeft), None, None, None)
    )
    queryTool.addPlayerShots(parThreeExample).unsafeRunSync() shouldBe 3
  }

  it should "fetch shots by Game" in {
    queryTool.getByGameAndMaybeHole(GameId(1), None).unsafeRunSync().size shouldBe 6
  }

  it should "fetch shots by Game and Holes" in {
    queryTool.getByGameAndMaybeHole(GameId(1), Some(Hole(8))).unsafeRunSync().size shouldBe 3
    queryTool.getByGameAndMaybeHole(GameId(1), Some(Hole(10))).unsafeRunSync().size shouldBe 0
  }

  it should "Add some shots and then replace" in {
    val parThreeExampleA: List[UserShotInput] = List(
      UserShotInput(GameId(1), Hole(11), 1, ParThree, Distance(310), TeeBox, FourIron, None, 17, Option(MiddleLeft), None, None, None),
      UserShotInput(GameId(1), Hole(11), 2, ParThree, Distance(20), OnTheGreen, Putter, None, 17, Option(LongLeft), None, None, None),
      UserShotInput(GameId(1), Hole(11), 3, ParThree, Distance(4), OnTheGreen, Putter, None, 17, Option(MiddleLeft), None, None, None)
    )
    val parThreeExampleB: List[UserShotInput] = List(
      UserShotInput(GameId(1), Hole(9), 1, ParThree, Distance(210), TeeBox, FourIron, None, 1, Option(MiddleLeft), None, None, None),
      UserShotInput(GameId(1), Hole(9), 2, ParThree, Distance(10), OnTheGreen, Putter, None, 1, Option(LongLeft), None, None, None),
      UserShotInput(GameId(1), Hole(9), 3, ParThree, Distance(2), OnTheGreen, Putter, None, 1, Option(MiddleLeft), None, None, None)
    )
    val parThreeExampleC: List[UserShotInput] = List(
      UserShotInput(GameId(1), Hole(9), 1, ParThree, Distance(210), TeeBox, FourIron, None, 2, Option(MiddleLeft), None, None, None),
      UserShotInput(GameId(1), Hole(9), 2, ParThree, Distance(10), OnTheGreen, Putter, None, 2, Option(LongLeft), None, None, None),
      UserShotInput(GameId(1), Hole(9), 3, ParThree, Distance(2), OnTheGreen, Putter, None, 2, Option(MiddleLeft), None, None, None)
    )
    val parThreeExampleD: List[UserShotInput] = List(
      UserShotInput(GameId(1), Hole(2), 1, ParThree, Distance(210), TeeBox, FourIron, None, 2, Option(MiddleLeft), None, None, None),
      UserShotInput(GameId(1), Hole(2), 2, ParThree, Distance(10), OnTheGreen, Putter, None, 2, Option(LongLeft), None, None, None)
    )

    queryTool.addPlayerShots(parThreeExampleA).unsafeRunSync() shouldBe 3
    queryTool.addPlayerShots(parThreeExampleB).unsafeRunSync() shouldBe 3
    queryTool.addPlayerShots(parThreeExampleC).unsafeRunSync() shouldBe 3
    queryTool.addPlayerShots(parThreeExampleD).unsafeRunSync() shouldBe 2
    queryTool.getByGameAndMaybeHole(GameId(1), Some(Hole(9))).unsafeRunSync().map(_.strokeIndex) shouldBe List(2, 2, 2)
  }

  it should "fetch all of the handicap histories" in {
    val expectedResult = List(Handicap(1.1), Handicap(7.1), Handicap(7.5))
    queryTool.getHandicapHistory(PlayerId(1)).unsafeRunSync().map(_.value) shouldBe expectedResult
  }

  it should "aggregate a games result correctly" in {
    val res = queryTool.aggregateGameResult(GameId(1)).unsafeRunSync()
    Helpers.combineAll(res.map(_.shotCount)) shouldBe 14
  }

  it should "add and get results" in {
    val gameRes = GolfResult(
      GameId(3),
      Birdie,
      Some(Strokes(1.2)),
      Some(Strokes(1.2)),
      Some(Strokes(1.2)),
      Some(Strokes(1.2)),
      Some(Strokes(1.2)),
      Points(3)
    )

    // Game
    queryTool.addResultByIdentifier(gameRes, None).unsafeRunSync() shouldBe 1
    queryTool.addResultByIdentifier(gameRes, None).unsafeRunSync() shouldBe 1

    queryTool.getResultByIdentifier(GameId(3), None).unsafeRunSync() shouldBe Some(gameRes)

    // Game + Hole
    queryTool.addResultByIdentifier(gameRes, Some(Hole(3))).unsafeRunSync() shouldBe 1
    queryTool.addResultByIdentifier(gameRes, Some(Hole(3))).unsafeRunSync() shouldBe 1

    queryTool.getResultByIdentifier(GameId(3), Some(Hole(3))).unsafeRunSync() shouldBe Some(gameRes)
  }

  it should "delete a players game" in {
    queryTool.deletePlayerGame(GameId(3)).unsafeRunSync() shouldBe 6
  }
}
