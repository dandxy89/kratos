package com.dandxy

import cats.effect._
import cats.syntax.all._
import com.dandxy.config._
import com.dandxy.db.util.HealthCheck
import com.dandxy.jwt.{ Claims, JwtAuthMiddleware }
import com.dandxy.db.{ MetricsStoreInterpreter, PGAPostgresQueryInterpreter, UserPostgresQueryInterpreter }
import com.dandxy.jwt.GenerateToken
import com.dandxy.service._
import doobie.util.ExecutionContexts
import io.circe.config.parser
import org.http4s.implicits._
import pdi.jwt.JwtAlgorithm
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.{ Router, Server => HTTP4Server }

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

object Server extends IOApp {

  implicit private val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  implicit private val t: Timer[IO] = IO.timer(ExecutionContext.global)

  def createServer[F[_]: ContextShift: ConcurrentEffect: Timer]: Resource[F, HTTP4Server[F]] =
    for {
      // Config
      conf <- Resource.liftF(parser.decodePathF[F, ApplicationConfig]("golf"))
      // Concurrency execution contexts
      conE <- ExecutionContexts.fixedThreadPool[F](conf.jdbc.connections.poolSize)
      cacE <- ExecutionContexts.cachedThreadPool[F]
      // JDBC Ops
      dbXa <- DatabaseConfig.hikariDbTransactor(conf.jdbc, conE, Blocker.liftExecutionContext(cacE))
      urDB = UserPostgresQueryInterpreter(dbXa, conf.auth)
      pgDB = PGAPostgresQueryInterpreter(dbXa)
      gsDB = MetricsStoreInterpreter(dbXa)
      dbStat <- Resource.liftF(HealthCheck.databaseStatusPoll(dbXa))
      // Routes
      hRoute        = HealthRoutes(dbStat)
      jwtMiddleware = JwtAuthMiddleware[F, Claims](conf.jwt.key, Seq(JwtAlgorithm.HS256))
      lRoute        = LoginRoute(urDB.attemptLogin, GenerateToken.prepareToken(conf.jwt.hours, conf.jwt.key))
      rRoute        = RegistrationRoute(urDB.registerUser)
      gRoute        = GolferRoutes(urDB, jwtMiddleware, pgDB.getStatistic)
      mRoute        = MetricsRoutes(gsDB, jwtMiddleware)
      // Router
      httpApp = Router(
        "/health"   -> hRoute.healthService,
        "/login"    -> lRoute.loginRoute,
        "/register" -> rRoute.registrationRoutes,
        "/golf"     -> gRoute.golferRoutes,
        "/metrics"  -> mRoute.metricsRoutes
      ).orNotFound
      // Database Migrations
      _ <- Resource.liftF(DatabaseConfig.initializeDb(conf.jdbc))
      // Service
      server <- BlazeServerBuilder[F]
        .bindHttp(conf.server.port, conf.server.host)
        .withHttpApp(httpApp)
        .withoutBanner
        .resource
    } yield server

  def run(args: List[String]): IO[ExitCode] =
    createServer
      .use(_ => IO.never)
      .as(ExitCode.Success)

}
