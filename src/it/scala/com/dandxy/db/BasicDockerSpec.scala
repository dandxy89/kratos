package com.dandxy.db

import com.dandxy.db.util.{HealthCheck, Migration}
import com.dandxy.db.util.HealthCheck.OK
import com.dandxy.util.PostgresDockerService
import org.scalatest.concurrent.Eventually
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

class BasicDockerSpec extends FlatSpec with Matchers with Eventually with BeforeAndAfterAll {

  val postgresDockerService = new PostgresDockerService(5444)

  eventually(postgresDockerService.isPostgresRunning shouldBe true)

  override def beforeAll(): Unit = Migration.flywayMigrateDatabase(postgresDockerService.config) match {
    case Left(error) => fail(error.getMessage)
    case Right(_)    => assert(true)
  }

  "HealthCheck" should "return the Status of the Database OK if it has started correctly" in {
    HealthCheck.queryStatus(postgresDockerService.postgresTransactor).unsafeRunSync() shouldBe OK
  }
}
