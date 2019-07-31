package com.dandxy

import cats.effect.{ContextShift, IO}
import com.dandxy.db.Migration._
import com.dandxy.db.PGAQueryTool
import com.dandxy.model.AppConfig
import com.dandxy.model.AppConfig.{ApplicationConfig, DBConfig}
import com.dandxy.model.golf.entity.Location.OnTheGreen
import com.dandxy.model.golf.input.Distance
import com.dandxy.model.golf.pga.Statistic.PGAStatistic
import doobie._
import doobie.implicits._
import io.circe.Error

import scala.concurrent.ExecutionContext

object MiniMain extends App {

  // Check Apply DB Migrations
  println(
    flywayMigrateDatabase(
      DBConfig("postgres", "docker", "localhost", 5432, "postgres")
    )
  )

  // Check Load Config
  val conf: Either[Error, ApplicationConfig] = AppConfig()
  println(conf)

  // Test DB
  implicit val cs: ContextShift[IO] = IO
    .contextShift(ExecutionContext.global)

  val maybeTrans = conf.map { c =>
    Transactor.fromDriverManager[IO](c.jdbc.driver, c.jdbc.url, c.jdbc.user, c.jdbc.password)
  }

  println(maybeTrans)

  val res: Either[Error, Option[PGAStatistic]] = maybeTrans.map { a =>
    PGAQueryTool
      .findStatistic(Distance(100), OnTheGreen)
      .transact(a)
      .unsafeRunSync()
  }

  println(res)

}
