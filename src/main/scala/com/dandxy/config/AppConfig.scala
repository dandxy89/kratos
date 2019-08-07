package com.dandxy.config

import com.dandxy.config.AppModels.{ AppName, ApplicationConfig, AuthSalt, DBConfig }
import com.typesafe.config.{ Config, ConfigFactory }
import io.circe.Error
import io.circe.config.syntax._
import io.circe.generic.auto._

object AppConfig {

  private[this] val config: Config                        = ConfigFactory.load()
  private[this] val jdbcConfig: Either[Error, DBConfig]   = config.as[DBConfig]("jdbc")
  private[this] val saltConfig: Either[Error, AuthSalt]   = config.as[AuthSalt]("auth")
  private[this] val appNameConfig: Either[Error, AppName] = config.as[AppName]("app")

  def apply(): Either[Error, ApplicationConfig] =
    for {
      dbA <- jdbcConfig
      aut <- saltConfig
      app <- appNameConfig
    } yield ApplicationConfig(dbA, app, aut)
}
