package com.dandxy.util

import java.sql.DriverManager
import java.util.UUID

import cats.effect.{ContextShift, IO}
import com.dandxy.config.AppModels.DBConfig
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux
import org.scalatest.concurrent.Eventually
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{Assertion, Matchers}

import scala.concurrent.ExecutionContext
import scala.sys.ShutdownHookThread
import scala.sys.process._
import scala.util.Try

class PostgresDockerService(customPort: Int) extends Eventually with Matchers {

  override implicit val patienceConfig: PatienceConfig = PatienceConfig(timeout = Span(60, Seconds), interval = Span(1, Seconds))

  private implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  val user        = "postgres"
  val password    = "docker"
  val driver      = "org.postgresql.Driver"
  val url         = s"jdbc:postgresql://localhost:$customPort/"
  val containerId = s"test-container-${UUID.randomUUID()}"

  def startUpPostgresDocker(): Assertion = {
    val _ = s"docker run --rm --name $containerId -p $customPort:5432 -e POSTGRES_PASSWORD=docker postgres:latest".run
    eventually(isPostgresRunning shouldBe true)
  }

  startUpPostgresDocker()
  initialiseShutdownHook()

  def isPostgresRunning: Boolean =
    Try {
      Option(DriverManager.getConnection(url, user, password)).map(_.close).isDefined
    }.getOrElse(false)

  private def initialiseShutdownHook(): ShutdownHookThread = sys.addShutdownHook {
    s"docker rm -f $containerId".run()
    eventually(isPostgresRunning shouldBe false)
  }

  val config: DBConfig = DBConfig(user, password, "localhost", customPort)

  val postgresTransactor: Aux[IO, Unit] = Transactor
    .fromDriverManager[IO](driver, url, user, password)

  def transactQuery[A](op: ConnectionIO[A]): A = op.transact(postgresTransactor).unsafeRunSync()

}