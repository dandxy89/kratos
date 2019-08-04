package com.dandxy.db

import com.dandxy.db.HealthCheck.OK
import com.dandxy.util.PostgresDockerService._
import org.scalatest.concurrent.Eventually
import org.scalatest.{ BeforeAndAfterAll, FlatSpec, Matchers }

class PostgresDockerSpec extends FlatSpec with Matchers with Eventually with BeforeAndAfterAll {

  eventually(isPostgresRunning shouldBe true)

  "PostgreSQL container" should "return the Status of the Database OK if it has started correctly" in {
    HealthCheck.queryStatus(postgresTransactor).unsafeRunSync() shouldBe OK
  }
}
