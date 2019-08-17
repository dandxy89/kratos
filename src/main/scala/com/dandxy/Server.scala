package com.dandxy

import cats.effect._
import cats.implicits._
import com.dandxy.config._
import com.dandxy.db.{ PGAPostgresQueryInterpreter, UserPostgresQueryInterpreter }
import com.dandxy.service.HealthRoutes
import doobie.util.ExecutionContexts
import io.circe.config.parser
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.{ Router, Server => HTTP4Server }

import scala.language.higherKinds

object Server extends IOApp {

  def createServer[F[_]: ContextShift: ConcurrentEffect: Timer]: Resource[F, HTTP4Server[F]] =
    for {
      conf <- Resource.liftF(parser.decodePathF[F, ApplicationConfig]("golf"))
      conE <- ExecutionContexts.fixedThreadPool[F](conf.jdbc.connections.poolSize)
      cacE <- ExecutionContexts.cachedThreadPool[F]
      dbXa <- DatabaseConfig.dbTransactor(conf.jdbc, conE, Blocker.liftExecutionContext(cacE))
      _       = UserPostgresQueryInterpreter(dbXa, conf.auth)
      _       = PGAPostgresQueryInterpreter(dbXa)
      ab      = HealthRoutes()
      httpApp = Router("/test" -> ab.pingPongService).orNotFound
      _ <- Resource.liftF(DatabaseConfig.initializeDb(conf.jdbc))
      server <- BlazeServerBuilder[F]
        .bindHttp(conf.server.port, conf.server.host)
        .withHttpApp(httpApp)
        .resource
    } yield server

  def run(args: List[String]): IO[ExitCode] =
    createServer.use(_ => IO.never).as(ExitCode.Success)

}
