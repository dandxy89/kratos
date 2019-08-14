package com.dandxy.db

import com.dandxy.db.util.HealthCheck.OK
import com.dandxy.db.util.{HealthCheck, Migration}
import com.dandxy.golf.entity.Location.{Bunker, Fairway, OnTheGreen, Recovery, Rough, TeeBox}
import com.dandxy.golf.entity.PGAStatistics
import com.dandxy.golf.input.Distance
import com.dandxy.golf.pga.Statistic.PGAStatistic
import com.dandxy.util.PostgresDockerService
import org.scalatest.concurrent.Eventually
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

class PGAQueryToolSpec extends FlatSpec with Matchers with Eventually with BeforeAndAfterAll {

  val service = new PostgresDockerService(5445)

  eventually(service.isPostgresRunning shouldBe true)

  override def beforeAll(): Unit = Migration.flywayMigrateDatabase(service.config) match {
    case Left(error) => fail(error.getMessage)
    case Right(m) =>
      println(s"UserQueryToolSpec migrations applied: $m")
      assert(true)
  }

  "HealthCheck" should "return the Status of the Database OK if it has started correctly" in {
    HealthCheck.queryStatus(service.postgresTransactor).unsafeRunSync() shouldBe OK
  }

  // Query Tool
  val queryTool = new PGAPostgresQueryTool(service.postgresTransactor)

  val pgaStats: (Distance, PGAStatistics) => Option[PGAStatistic] =
    (d, s) => queryTool.getStatistic(d, s).unsafeRunSync()

  "PGAQueryTool" should "return the correct values" in {
    // Positive
    pgaStats(Distance(400), TeeBox) shouldBe Some(PGAStatistic(Distance(400.0), 3.99))
    pgaStats(Distance(320), Fairway) shouldBe Some(PGAStatistic(Distance(320.0), 3.84))
    pgaStats(Distance(100), Rough) shouldBe Some(PGAStatistic(Distance(100.0), 3.02))
    pgaStats(Distance(30), Bunker) shouldBe Some(PGAStatistic(Distance(30.0), 2.66))
    pgaStats(Distance(50), Recovery) shouldBe Some(PGAStatistic(Distance(50.0), 3.79))
    pgaStats(Distance(50), OnTheGreen) shouldBe Some(PGAStatistic(Distance(50.0), 2.135))

    // Negative
    pgaStats(Distance(310), Fairway) shouldBe None
    pgaStats(Distance(500), OnTheGreen) shouldBe None
  }
}
