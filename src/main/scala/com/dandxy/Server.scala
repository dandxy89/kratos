package com.dandxy

import cats.effect._
import cats.implicits._
import com.dandxy.config._
import com.dandxy.db.util.HealthCheck
import com.dandxy.db.{ PGAPostgresQueryInterpreter, UserPostgresQueryInterpreter }
import com.dandxy.jwt.GenerateToken
import com.dandxy.service.{ GolferRoutes, HealthRoutes, LoginRoute, RegistrationRoute }
import doobie.util.ExecutionContexts
import io.circe.config.parser
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.{ Router, Server => HTTP4Server }

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

object Server extends IOApp {

  implicit private val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  implicit private val t: Timer[IO] = IO.timer(ExecutionContext.global)

  def createServer[F[_]: ContextShift: ConcurrentEffect: Timer]: Resource[F, HTTP4Server[F]] =
    for {
      conf <- Resource.liftF(parser.decodePathF[F, ApplicationConfig]("golf"))
      conE <- ExecutionContexts.fixedThreadPool[F](conf.jdbc.connections.poolSize)
      cacE <- ExecutionContexts.cachedThreadPool[F]
      dbXa <- DatabaseConfig.dbTransactor(conf.jdbc, conE, Blocker.liftExecutionContext(cacE))
      urDB = UserPostgresQueryInterpreter(dbXa, conf.auth)
      _    = PGAPostgresQueryInterpreter(dbXa)
      dbStat <- Resource.liftF(HealthCheck.databaseStatusPoll(dbXa))
      hRoute = HealthRoutes(dbStat)
      lRoute = LoginRoute(urDB.attemptLogin, GenerateToken.prepareToken(conf.jwt.key))
      rRoute = RegistrationRoute(urDB.registerUser)
      gRoute = GolferRoutes(urDB, conf.jwt.key)
      httpApp = Router(
        "/health"   -> hRoute.healthService,
        "/login"    -> lRoute.loginRoute,
        "/register" -> rRoute.registrationRoutes,
        "/golf"     -> gRoute.golferRoutes
      ).orNotFound
      _ <- Resource.liftF(DatabaseConfig.initializeDb(conf.jdbc))
      server <- BlazeServerBuilder[F]
        .bindHttp(conf.server.port, conf.server.host)
        .withHttpApp(httpApp)
        .resource
    } yield server

  def run(args: List[String]): IO[ExitCode] =
    createServer
      .use(_ => IO.never)
      .as(ExitCode.Success)

}
