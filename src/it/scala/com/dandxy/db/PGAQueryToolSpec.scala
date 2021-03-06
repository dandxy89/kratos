package com.dandxy.db

import cats.effect.{ IO, Timer }
import cats.implicits._
import com.dandxy.config.DatabaseConfig
import com.dandxy.db.util.HealthCheck
import com.dandxy.db.util.HealthCheck.OK
import com.dandxy.golf.entity.Location
import com.dandxy.golf.entity.Location.{ Bunker, Fairway, OnTheGreen, Recovery, Rough, TeeBox }
import com.dandxy.golf.input.Distance
import com.dandxy.golf.pga.Statistic.PGAStatistic
import com.dandxy.util.PostgresDockerService
import org.scalatest.concurrent.Eventually
import org.scalatest.{ BeforeAndAfterAll, FlatSpec, Matchers }

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class PGAQueryToolSpec extends FlatSpec with Matchers with Eventually with BeforeAndAfterAll {

  val service = new PostgresDockerService(5445)

  implicit val t: Timer[IO] = IO.timer(ExecutionContext.global)

  eventually(service.isPostgresRunning shouldBe true)

  override def beforeAll(): Unit =
    (DatabaseConfig
      .initializeDb[IO](service.config) *> IO.sleep(2.seconds)).unsafeRunSync()

  "HealthCheck" should "return the Status of the Database OK if it has started correctly" in {
    HealthCheck.queryStatus(service.postgresTransactor).unsafeRunSync() shouldBe OK
  }

  // Query Tool
  val queryTool = PGAPostgresQueryInterpreter(service.postgresTransactor)

  val pgaStats: (Distance, Location) => Option[PGAStatistic] =
    (d, l) => queryTool.getStatistic(d, l).unsafeRunSync()

  "PGAQueryTool" should "return the correct values" in {
    // Positive
    pgaStats(Distance(400), TeeBox) shouldBe Some(PGAStatistic(Distance(400.0), 3.99))
    pgaStats(Distance(320), Fairway) shouldBe Some(PGAStatistic(Distance(320.0), 3.84))
    pgaStats(Distance(100), Rough) shouldBe Some(PGAStatistic(Distance(100.0), 3.02))
    pgaStats(Distance(30), Bunker) shouldBe Some(PGAStatistic(Distance(30.0), 2.66))
    pgaStats(Distance(50), Recovery) shouldBe Some(PGAStatistic(Distance(50.0), 3.79))
    pgaStats(Distance(50), OnTheGreen) shouldBe Some(PGAStatistic(Distance(50.0), 2.135))

    // New interpolated data
    pgaStats(Distance(310), Fairway) shouldBe Some(PGAStatistic(Distance(310.0), 3.819))

    // Outside of range
    pgaStats(Distance(500), OnTheGreen) shouldBe None
  }
}
