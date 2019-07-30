package com.dandxy.db

import cats.implicits._
import com.dandxy.model.AppConfig.DBConfig
import com.typesafe.scalalogging.LazyLogging
import org.flywaydb.core.Flyway

import scala.util.control.NonFatal

object Migration extends LazyLogging {

  def flywayMigrateDatabase(dbConfig: DBConfig): Either[Throwable, Int] =
    Either.catchNonFatal {
      val fw = Flyway
        .configure()
        .dataSource(dbConfig.url, dbConfig.user, dbConfig.password)
        .load()

      val migrationsApplied: Int = fw.migrate()

      logger.info(
        s"Successfully applied $migrationsApplied migrations to the database"
      )
      migrationsApplied
    }.leftMap {
      case NonFatal(t) =>
        logger.error("Unable to run Flyway migration")
        t
    }
}
