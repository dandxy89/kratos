package com.dandxy.db

import java.sql.Timestamp

import com.dandxy.auth.Salt
import com.dandxy.config.AppModels.AuthSalt
import com.dandxy.db.util.HealthCheck.OK
import com.dandxy.db.util.{ HealthCheck, Migration }
import com.dandxy.model.golf.entity.GolfClub.Driver
import com.dandxy.model.golf.entity.Manufacturer.Miura
import com.dandxy.model.golf.input.Distance
import com.dandxy.model.golf.input.DistanceMeasurement.Yards
import com.dandxy.model.golf.input.ShotHeight.Low
import com.dandxy.model.golf.input.ShotShape.Straight
import com.dandxy.model.user._
import com.dandxy.util.PostgresDockerService
import org.scalatest.concurrent.Eventually
import org.scalatest.{ BeforeAndAfterAll, FlatSpec, Matchers }

class UserQueryToolSpec extends FlatSpec with Matchers with Eventually with BeforeAndAfterAll {

  val service = new PostgresDockerService(5446)

  eventually(service.isPostgresRunning shouldBe true)

  override def beforeAll(): Unit = Migration.flywayMigrateDatabase(service.config) match {
    case Left(error) => fail(error.getMessage)
    case Right(_)    => assert(true)
  }

  val testTS         = new Timestamp(1565127644)
  val testEmail      = UserEmail("it@golf.com")
  val reg            = UserRegistration(testEmail, "it", "isFun")
  val pass           = Password("testPassword")
  val testGolfClubs  = List(GolfClubData(PlayerId(1), Driver, Some(Straight), Some(Low), Some(Miura), Distance(200.0), Yards))
  val testGolfClubs2 = List(GolfClubData(PlayerId(3), Driver, Some(Straight), Some(Low), Some(Miura), Distance(200.0), Yards))
  val testPlayer     = PlayerId(3)
  val fakeEmail      = UserEmail("fake@hacker.com")
  val authSalt       = AuthSalt(Some(Salt("testingSalt")))

  "HealthCheck" should "return the Status of the Database OK if it has started correctly" in {
    HealthCheck.queryStatus(service.postgresTransactor).unsafeRunSync() shouldBe OK
  }

  "UserQueryTool" should "comply with GDPR regulations" in {
    service.transactQuery(UserQueryTool.gdprPurge(PlayerId(2))) shouldBe 7
  }

  it should "register a user correctly" in {
    val passwordValue = "$argon2id$v=19$m=65536,t=50,p=8$F7G2ZwXteYVMD/Xo5TPZ8A$8Ts3ZLAK67ED5Kb1ocT3iGogZd68s74lhanhUwVCVF0"
    service.transactQuery(UserQueryTool.registerUser(reg, Password(passwordValue), testTS)) shouldBe testPlayer
  }

  it should "login a user correctly" in {
    service.transactQuery(UserQueryTool.attemptLogin(authSalt)(testEmail, pass)) shouldBe Some(testPlayer)
  }

  it should "return None when a user doesn't exist" in {
    service.transactQuery(UserQueryTool.attemptLogin(authSalt)(fakeEmail, pass)) shouldBe None
  }

  it should "add club data correctly" in {
    service.transactQuery(UserQueryTool.addClubData(testPlayer, testGolfClubs2)) shouldBe 1
  }

  it should "get club data into a list correctly" in {
    service.transactQuery(UserQueryTool.getUserClubs(PlayerId(1))) shouldBe testGolfClubs
    service.transactQuery(UserQueryTool.getUserClubs(testPlayer)) shouldBe testGolfClubs2
  }
}
