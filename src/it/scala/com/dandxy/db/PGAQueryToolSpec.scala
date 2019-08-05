package com.dandxy.db

import com.dandxy.db.HealthCheck.OK
import com.dandxy.model.golf.entity.Location._
import com.dandxy.model.golf.entity.PGAStatistics
import com.dandxy.model.golf.input.Distance
import com.dandxy.model.golf.pga.Statistic
import com.dandxy.model.golf.pga.Statistic.PGAStatistic
import com.dandxy.util.PostgresDockerService
import doobie.implicits._
import org.scalatest.concurrent.Eventually
import org.scalatest.{ BeforeAndAfterAll, FlatSpec, Matchers }

class PGAQueryToolSpec extends FlatSpec with Matchers with Eventually with BeforeAndAfterAll {

  val postgresDockerService = new PostgresDockerService(5445)

  eventually(postgresDockerService.isPostgresRunning shouldBe true)

  override def beforeAll(): Unit = Migration.flywayMigrateDatabase(postgresDockerService.config) match {
    case Left(error) => fail(error.getMessage)
    case Right(_)    => assert(true)
  }

  "HealthCheck" should "return the Status of the Database OK if it has started correctly" in {
    HealthCheck.queryStatus(postgresDockerService.postgresTransactor).unsafeRunSync() shouldBe OK
  }

  def transactQuery(distance: Distance, location: PGAStatistics): Option[Statistic.PGAStatistic] =
    PGAQueryTool
      .findStatistic(distance, location)
      .transact(postgresDockerService.postgresTransactor)
      .unsafeRunSync()

  "PGAQueryTool" should "return the correct values" in {
    // Positive
    transactQuery(Distance(400), TeeBox) shouldBe Some(PGAStatistic(Distance(400.0),3.99))
    transactQuery(Distance(320), Fairway) shouldBe Some(PGAStatistic(Distance(320.0),3.84))
    transactQuery(Distance(100), Rough) shouldBe Some(PGAStatistic(Distance(100.0),3.02))
    transactQuery(Distance(30), Bunker) shouldBe Some(PGAStatistic(Distance(30.0),2.66))
    transactQuery(Distance(50), Recovery) shouldBe Some(PGAStatistic(Distance(50.0),3.79))
    transactQuery(Distance(50), OnTheGreen) shouldBe Some(PGAStatistic(Distance(50.0),2.135))

    // Negative
    transactQuery(Distance(310), Fairway) shouldBe None // TODO: Add one yard increments
    transactQuery(Distance(500), OnTheGreen) shouldBe None
  }
}
