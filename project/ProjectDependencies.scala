import sbt.{ ModuleID, compilerPlugin, _ }

object ProjectDependencies extends {

  private[this] object Version {
    val dockerITVersion    = "0.9.9"
    val fS2Version         = "1.0.4"
    val http4sVersion      = "0.20.1"
    val circeVersion       = "0.12.0-M1"
    val logbackVersion     = "1.2.3"
    val scalaLogVersion    = "3.9.2"
    val pureConfigVersion  = "0.10.2"
    val zioVersion         = "1.0-RC4"
    val scalaTestVersion   = "3.0.5"
    val doobieVersion      = "0.7.0-M5"
    val h2Version          = "1.4.199"
    val flywayVersion      = "5.2.4"
    val refindedVersion    = "0.9.8"
    val scala4jLogging     = "1.7.26"
    val circeConfigVersion = "0.6.1"
  }

  def apply(): Seq[ModuleID] = Seq(
    "com.whisk"                  %% "docker-testkit-scalatest"        % Version.dockerITVersion,
    "com.whisk"                  %% "docker-testkit-impl-docker-java" % Version.dockerITVersion,
    "co.fs2"                     %% "fs2-core"                        % Version.fS2Version,
    "co.fs2"                     %% "fs2-io"                          % Version.fS2Version,
    "org.http4s"                 %% "http4s-blaze-server"             % Version.http4sVersion,
    "org.http4s"                 %% "http4s-blaze-client"             % Version.http4sVersion,
    "org.http4s"                 %% "http4s-circe"                    % Version.http4sVersion,
    "org.http4s"                 %% "http4s-dsl"                      % Version.http4sVersion,
    "io.circe"                   %% "circe-generic"                   % Version.circeVersion,
    "io.circe"                   %% "circe-config"                    % Version.circeConfigVersion,
    "org.tpolecat"               %% "doobie-core"                     % Version.doobieVersion,
    "org.tpolecat"               %% "doobie-hikari"                   % Version.doobieVersion,
    "org.tpolecat"               %% "doobie-postgres"                 % Version.doobieVersion,
    "com.h2database"             % "h2"                               % Version.h2Version,
    "org.flywaydb"               % "flyway-core"                      % Version.flywayVersion,
    "org.slf4j"                  % "slf4j-log4j12"                    % Version.scala4jLogging,
    "com.typesafe.scala-logging" %% "scala-logging"                   % Version.scalaLogVersion,
    "com.github.pureconfig"      %% "pureconfig"                      % Version.pureConfigVersion,
    "com.github.pureconfig"      %% "pureconfig-cats-effect"          % Version.pureConfigVersion,
    "eu.timepit"                 %% "refined"                         % Version.refindedVersion,
    "eu.timepit"                 %% "refined-cats"                    % Version.refindedVersion,
    "org.scalaz"                 %% "scalaz-zio"                      % Version.zioVersion,
    "org.scalaz"                 %% "scalaz-zio-interop-cats"         % Version.zioVersion,
    "org.scalatest"              %% "scalatest"                       % Version.scalaTestVersion % "test",
    compilerPlugin("org.spire-math" %% "kind-projector"     % "0.9.4"),
    compilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.3.0-M4")
  )
}
