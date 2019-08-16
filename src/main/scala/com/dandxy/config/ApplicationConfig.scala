package com.dandxy.config

final case class ApplicationConfig(jdbc: DatabaseConfig, auth: AuthSalt)
