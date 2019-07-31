import sbt.{ compilerPlugin, ModuleID }
import sbt._

object ProjectDependencies extends {

  private[this] object Version {
    val dockerITVersion    = "0.9.9"
    val FS2Version         = "1.0.4"
    val Http4sVersion      = "0.20.1"
    val CirceVersion       = "0.12.0-M1"
    val LogbackVersion     = "1.2.3"
    val ScalaLogVersion    = "3.9.2"
    val PureConfigVersion  = "0.10.2"
    val ZioVersion         = "1.0-RC4"
    val ScalaTestVersion   = "3.0.5"
    val DoobieVersion      = "0.7.0-M5"
    val H2Version          = "1.4.199"
    val FlywayVersion      = "5.2.4"
    val RefindedVersion    = "0.9.8"
    val Scala4jLogging     = "1.7.26"
    val CirceConfigVersion = "0.6.1"
  }

  def apply(): Seq[ModuleID] = Seq(
    "com.whisk"                  %% "docker-testkit-scalatest"    % Version.dockerITVersion,
    "com.whisk"                  %% "docker-testkit-impl-spotify" % Version.dockerITVersion,
    "co.fs2"                     %% "fs2-core"                    % Version.FS2Version,
    "co.fs2"                     %% "fs2-io"                      % Version.FS2Version,
    "org.http4s"                 %% "http4s-blaze-server"         % Version.Http4sVersion,
    "org.http4s"                 %% "http4s-blaze-client"         % Version.Http4sVersion,
    "org.http4s"                 %% "http4s-circe"                % Version.Http4sVersion,
    "org.http4s"                 %% "http4s-dsl"                  % Version.Http4sVersion,
    "io.circe"                   %% "circe-generic"               % Version.CirceVersion,
    "io.circe"                   %% "circe-config"                % Version.CirceConfigVersion,
    "org.tpolecat"               %% "doobie-core"                 % Version.DoobieVersion,
    "org.tpolecat"               %% "doobie-hikari"               % Version.DoobieVersion,
    "org.tpolecat"               %% "doobie-postgres"             % Version.DoobieVersion,
    "com.h2database"             % "h2"                           % Version.H2Version,
    "org.flywaydb"               % "flyway-core"                  % Version.FlywayVersion,
    "org.slf4j"                  % "slf4j-log4j12"                % Version.Scala4jLogging,
    "com.typesafe.scala-logging" %% "scala-logging"               % Version.ScalaLogVersion,
    "com.github.pureconfig"      %% "pureconfig"                  % Version.PureConfigVersion,
    "com.github.pureconfig"      %% "pureconfig-cats-effect"      % Version.PureConfigVersion,
    "eu.timepit"                 %% "refined"                     % Version.RefindedVersion,
    "eu.timepit"                 %% "refined-cats"                % Version.RefindedVersion,
    "org.scalaz"                 %% "scalaz-zio"                  % Version.ZioVersion,
    "org.scalaz"                 %% "scalaz-zio-interop-cats"     % Version.ZioVersion,
    "org.scalatest"              %% "scalatest"                   % Version.ScalaTestVersion % "test",
    compilerPlugin("org.spire-math" %% "kind-projector"     % "0.9.4"),
    compilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.3.0-M4")
  )
}
