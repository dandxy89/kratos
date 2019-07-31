package com.dandxy.model

import com.typesafe.config.{Config, ConfigFactory}
import io.circe.Error
import io.circe.config.syntax._
import io.circe.generic.auto._

object AppConfig {

  // Model
  final case class DBConfig(user: String, password: String, host: String, port: Int, databaseName: String) {
    val url: String    = s"jdbc:postgresql://$host:$port/$databaseName"
    val driver: String = "org.postgresql.Driver"
  }

  // Application Config
  final case class ApplicationConfig(jdbc: DBConfig, name: String)

  // Loading Config
  private val config: Config = ConfigFactory.load()

  private val jdbcConfig: Either[Error, DBConfig] = config.as[DBConfig]("jdbc")

  def apply(): Either[Error, ApplicationConfig] =
    for {
      db <- jdbcConfig
    } yield ApplicationConfig(db, "TODO")

}
