package com.dandxy.config

import cats.effect._
import cats.syntax.functor._
import com.typesafe.scalalogging.StrictLogging
import doobie.hikari.HikariTransactor
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.flywaydb.core.Flyway

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

final case class DatabaseConfig(driver: String,
                                host: String,
                                port: Int,
                                user: String,
                                password: String,
                                connections: DatabaseConnectionsConfig)

object DatabaseConfig extends StrictLogging {

  def hikariDbTransactor[F[_]: Async: ContextShift](dbc: DatabaseConfig,
                                                    connEc: ExecutionContext,
                                                    blocker: Blocker): Resource[F, HikariTransactor[F]] =
    HikariTransactor.newHikariTransactor[F](
      dbc.driver,
      s"jdbc:postgresql://${dbc.host}:${dbc.port}/",
      dbc.user,
      dbc.password,
      connEc,
      blocker.blockingContext
    )

  def initializeDb[F[_]](cfg: DatabaseConfig)(implicit S: Sync[F]): F[Unit] =
    S.delay {
      val fw: Flyway = Flyway
        .configure()
        .dataSource(s"jdbc:postgresql://${cfg.host}:${cfg.port}/", cfg.user, cfg.password)
        .load()
      val m = fw.migrate()
      logger.info(s"$m Migrations applied to the database")
    }.as(())

  implicit val decoder: Decoder[DatabaseConfig] = deriveDecoder

}
