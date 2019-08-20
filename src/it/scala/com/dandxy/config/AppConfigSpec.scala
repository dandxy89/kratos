package com.dandxy.config

import com.dandxy.auth.Salt
import io.circe.Error
import io.circe.config.parser
import org.scalatest.{FlatSpec, Matchers}

//noinspection SpellCheckingInspection
class AppConfigSpec extends FlatSpec with Matchers {

  behavior of "Configuration file"

  it should "Load the config correctly - and be used as a template for the main" in {
    val readConfig: Either[Error, ApplicationConfig] = parser.decodePath[ApplicationConfig]("golf")
    val check: ApplicationConfig = readConfig
      .getOrElse(fail(s"Unable to parse config: $readConfig"))

    // Auth
    //noinspection SpellCheckingInspection
    check.auth.salt shouldBe Some(
      Salt("$argon2id$v=19$m=65536,t=50,p=8$F7G2ZwXteYVMD/Xo5TPZ8A$8Ts3ZLAK67ED5Kb1ocT3iGogZd68s74lhanhUwVCVF0")
    )

    // JDBC
    check.jdbc.driver shouldBe "org.postgresql.Driver"
    check.jdbc.password shouldBe "docker"
    check.jdbc.user shouldBe "postgres"
  }
}
