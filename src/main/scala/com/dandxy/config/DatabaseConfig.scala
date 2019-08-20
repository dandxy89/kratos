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

final case class DatabaseConfig(
  driver: String,
  host: String,
  port: Int,
  user: String,
  password: String,
  connections: DatabaseConnectionsConfig
) {
  val url = s"jdbc:postgresql://$host:$port/"
}

object DatabaseConfig extends StrictLogging {

  def dbTransactor[F[_]: Async: ContextShift](
    dbc: DatabaseConfig,
    connEc: ExecutionContext,
    blocker: Blocker
  ): Resource[F, HikariTransactor[F]] =
    HikariTransactor.newHikariTransactor[F](dbc.driver, dbc.url, dbc.user, dbc.password, connEc, blocker.blockingContext)

  def initializeDb[F[_]](cfg: DatabaseConfig)(implicit S: Sync[F]): F[Unit] =
    S.delay {
      val fw: Flyway = Flyway
        .configure()
        .dataSource(cfg.url, cfg.user, cfg.password)
        .load()
      val m = fw.migrate()
      logger.info(s"$m Migrations applied to the database")
    }.as(())

  implicit val decoder: Decoder[DatabaseConfig] = deriveDecoder

}
