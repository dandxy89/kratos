package com.dandxy.util

import java.sql.DriverManager
import java.util.UUID

import cats.effect.{ ContextShift, IO }
import com.dandxy.model.AppConfig.DBConfig
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux
import org.scalatest.concurrent.Eventually
import org.scalatest.time.{ Seconds, Span }
import org.scalatest.{ Assertion, Matchers }

import scala.concurrent.ExecutionContext
import scala.sys.ShutdownHookThread
import scala.sys.process._
import scala.util.Try

object PostgresDockerService extends Eventually with Matchers {

  override implicit val patienceConfig: PatienceConfig = PatienceConfig(timeout = Span(60, Seconds), interval = Span(1, Seconds))

  val url      = "jdbc:postgresql://localhost:5432/"
  val user     = "postgres"
  val password = "docker"
  val driver   = "org.postgresql.Driver"

  val containerId = s"test-container-${UUID.randomUUID()}"

  def startUpPostgresDocker(): Assertion = {
    val _ = s"docker run --rm --name $containerId -p 5432:5432 -e POSTGRES_PASSWORD=docker postgres:latest".run
    eventually(isPostgresRunning shouldBe true)
  }

  startUpPostgresDocker()
  initialiseShutdownHook()

  def isPostgresRunning: Boolean = {
    println("Connecting to postgres to see if it is up and running...")
    Try {
      Option(DriverManager.getConnection(url, user, password)).map(_ close ()).isDefined
    }.getOrElse(false)
  }

  private def initialiseShutdownHook(): ShutdownHookThread =
    sys.addShutdownHook {
      s"docker rm -f $containerId".run()
      eventually(isPostgresRunning shouldBe false)
    }

  private implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  val config: DBConfig = DBConfig("postgres", "password", "localhost", 5432, "postgresdb")

  val postgresTransactor: Aux[IO, Unit] = Transactor
    .fromDriverManager[IO](
      PostgresDockerService.driver,
      PostgresDockerService.url,
      PostgresDockerService.user,
      PostgresDockerService.password
    )
}
