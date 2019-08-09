package com.dandxy.db

import com.dandxy.db.util.Migration
import com.dandxy.model.golf.entity.Location._
import com.dandxy.model.golf.entity.PGAStatistics
import com.dandxy.model.golf.input.Distance
import com.dandxy.model.golf.pga.Statistic.PGAStatistic
import com.dandxy.util.PostgresDockerService
import doobie.free.connection.ConnectionIO
import org.scalatest.concurrent.Eventually
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

class PGAQueryToolSpec extends FlatSpec with Matchers with Eventually with BeforeAndAfterAll {

  val service = new PostgresDockerService(5445)

  eventually(service.isPostgresRunning shouldBe true)

  override def beforeAll(): Unit = Migration.flywayMigrateDatabase(service.config) match {
    case Left(error) => fail(error.getMessage)
    case Right(m)    =>
      println(s"UserQueryToolSpec migrations applied: $m")
      assert(true)
  }

  val pgaStats: (Distance, PGAStatistics) => ConnectionIO[Option[PGAStatistic]] =
    (d, s) => PGAQueryTool.getStatistic(d, s)

  "PGAQueryTool" should "return the correct values" in {
    // Positive
    service.transactQuery(pgaStats(Distance(400), TeeBox)) shouldBe Some(PGAStatistic(Distance(400.0), 3.99))
    service.transactQuery(pgaStats(Distance(320), Fairway)) shouldBe Some(PGAStatistic(Distance(320.0), 3.84))
    service.transactQuery(pgaStats(Distance(100), Rough)) shouldBe Some(PGAStatistic(Distance(100.0), 3.02))
    service.transactQuery(pgaStats(Distance(30), Bunker)) shouldBe Some(PGAStatistic(Distance(30.0), 2.66))
    service.transactQuery(pgaStats(Distance(50), Recovery)) shouldBe Some(PGAStatistic(Distance(50.0), 3.79))
    service.transactQuery(pgaStats(Distance(50), OnTheGreen)) shouldBe Some(PGAStatistic(Distance(50.0), 2.135))

    // Negative
    service.transactQuery(pgaStats(Distance(310), Fairway)) shouldBe None
    service.transactQuery(pgaStats(Distance(500), OnTheGreen)) shouldBe None
  }
}
