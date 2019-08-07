package com.dandxy.config

import com.dandxy.auth.Salt

object AppModels {

  final case class DBConfig(user: String, password: String, host: String, port: Int) {
    val url: String    = s"jdbc:postgresql://$host:$port/"
    val driver: String = "org.postgresql.Driver"
  }

  final case class AppName(name: String)

  final case class AuthSalt(salt: Option[Salt])

  final case class ApplicationConfig(jdbc: DBConfig, name: AppName, authSalt: AuthSalt)

}
