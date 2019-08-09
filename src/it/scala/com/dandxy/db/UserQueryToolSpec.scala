package com.dandxy.db

import java.sql.Timestamp

import com.dandxy.auth.Salt
import com.dandxy.config.AppModels.AuthSalt
import com.dandxy.db.util.Migration
import com.dandxy.model.golf.entity.GolfClub.{ Driver, FourIron, Putter }
import com.dandxy.model.golf.entity.Hole
import com.dandxy.model.golf.entity.Location.{ OnTheGreen, TeeBox }
import com.dandxy.model.golf.entity.Manufacturer.Miura
import com.dandxy.model.golf.entity.Orientation.{ LongLeft, MiddleLeft }
import com.dandxy.model.golf.entity.Par.ParThree
import com.dandxy.model.golf.input.DistanceMeasurement.Yards
import com.dandxy.model.golf.input.GolfInput.{ UserGameInput, UserShotInput }
import com.dandxy.model.golf.input.ShotHeight.Low
import com.dandxy.model.golf.input.ShotShape.Straight
import com.dandxy.model.golf.input.{ Distance, Handicap, WindSpeed }
import com.dandxy.model.user._
import com.dandxy.util.PostgresDockerService
import org.scalatest.concurrent.Eventually
import org.scalatest.{ BeforeAndAfterAll, FlatSpec, Matchers }

class UserQueryToolSpec extends FlatSpec with Matchers with Eventually with BeforeAndAfterAll {

  val service = new PostgresDockerService(5446)

  eventually(service.isPostgresRunning shouldBe true)

  override def beforeAll(): Unit = Migration.flywayMigrateDatabase(service.config) match {
    case Left(error) => fail(error.getMessage)
    case Right(m) =>
      println(s"UserQueryToolSpec migrations applied: $m")
      assert(true)
  }

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
  val fakeEmail = UserEmail("fake@hacker.com")
  val authSalt  = AuthSalt(Some(Salt("testingSalt")))

  "UserQueryTool" should "comply with GDPR regulations" in {
    service.transactQuery(UserQueryTool.gdprPurge(PlayerId(2))) shouldBe 7
  }

  it should "register a user correctly" in {
    val passwordValue = "$argon2id$v=19$m=65536,t=50,p=8$F7G2ZwXteYVMD/Xo5TPZ8A$8Ts3ZLAK67ED5Kb1ocT3iGogZd68s74lhanhUwVCVF0"
    service.transactQuery(UserQueryTool.registerUser(reg, Password(passwordValue), testTS)) shouldBe userThree
  }

  it should "login a user correctly" in {
    service.transactQuery(UserQueryTool.attemptLogin(authSalt)(testEmail, pass)) shouldBe Some(userThree)
  }

  it should "return None when a user doesn't exist" in {
    service.transactQuery(UserQueryTool.attemptLogin(authSalt)(fakeEmail, pass)) shouldBe None
  }

  it should "add club data correctly" in {
    service.transactQuery(UserQueryTool.addClubData(userThree, testGolfClubs2)) shouldBe 1
  }

  it should "get club data into a list correctly" in {
    service.transactQuery(UserQueryTool.getUserClubs(userOne)) shouldBe testGolfClubs
    service.transactQuery(UserQueryTool.getUserClubs(userThree)) shouldBe testGolfClubs2
  }

  it should "get all of the players games in the database" in {
    service
      .transactQuery(UserQueryTool.getAllPlayerGames(userOne))
      .map(_.gameId) shouldBe List(Some(GameId(1)), Some(GameId(3)))
  }

  it should "get one of the players games in the database" in {
    service
      .transactQuery(UserQueryTool.getPlayerGame(GameId(1)))
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
    service.transactQuery(UserQueryTool.addPlayerGame(gameTest)) shouldBe GameId(4)
  }

  it should "fetch shots by Game - shouldBe Nil" in {
    service.transactQuery(UserQueryTool.fetchByGameAndMaybeHole(GameId(4), None)) shouldBe Nil
  }

  it should "add Player Shots" in {
    val parThreeExample: List[UserShotInput] = List(
      UserShotInput(GameId(1), Hole(8), 1, ParThree, Distance(210), TeeBox, FourIron, None, 1, Option(MiddleLeft), None, None, None),
      UserShotInput(GameId(1), Hole(8), 2, ParThree, Distance(10), OnTheGreen, Putter, None, 1, Option(LongLeft), None, None, None),
      UserShotInput(GameId(1), Hole(8), 3, ParThree, Distance(2), OnTheGreen, Putter, None, 1, Option(MiddleLeft), None, None, None)
    )
    service.transactQuery(UserQueryTool.addPlayerShots(parThreeExample)) shouldBe 3
  }

  it should "fetch shots by Game" in {
    service.transactQuery(UserQueryTool.fetchByGameAndMaybeHole(GameId(1), None)).size shouldBe 6
  }

  it should "fetch shots by Game and Holes" in {
    service.transactQuery(UserQueryTool.fetchByGameAndMaybeHole(GameId(1), Some(Hole(8)))).size shouldBe 3
    service.transactQuery(UserQueryTool.fetchByGameAndMaybeHole(GameId(1), Some(Hole(10)))).size shouldBe 0
  }

  it should "Add some shots and then replace" in {
    val parThreeExample: List[UserShotInput] = List(
      UserShotInput(GameId(1), Hole(9), 1, ParThree, Distance(210), TeeBox, FourIron, None, 1, Option(MiddleLeft), None, None, None),
      UserShotInput(GameId(1), Hole(9), 2, ParThree, Distance(10), OnTheGreen, Putter, None, 1, Option(LongLeft), None, None, None),
      UserShotInput(GameId(1), Hole(9), 3, ParThree, Distance(2), OnTheGreen, Putter, None, 1, Option(MiddleLeft), None, None, None)
    )
    val parThreeExample2: List[UserShotInput] = List(
      UserShotInput(GameId(1), Hole(9), 1, ParThree, Distance(210), TeeBox, FourIron, None, 2, Option(MiddleLeft), None, None, None),
      UserShotInput(GameId(1), Hole(9), 2, ParThree, Distance(10), OnTheGreen, Putter, None, 2, Option(LongLeft), None, None, None),
      UserShotInput(GameId(1), Hole(9), 3, ParThree, Distance(2), OnTheGreen, Putter, None, 2, Option(MiddleLeft), None, None, None)
    )
    service.transactQuery(UserQueryTool.addPlayerShots(parThreeExample)) shouldBe 3
    service.transactQuery(UserQueryTool.addPlayerShots(parThreeExample2)) shouldBe 3
  }
}
