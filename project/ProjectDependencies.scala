import sbt.{ ModuleID, compilerPlugin, _ }

object ProjectDependencies extends {

  private[this] object Version {
    val argonPass          = "2.5"
    val circeVersion       = "0.11.1"
    val doobieVersion      = "0.7.0-M5"
    val fS2Version         = "1.0.4"
    val http4sVersion      = "0.20.10"
    val JwtVersion         = "3.1.0"
    val logbackVersion     = "1.2.3"
    val scalaLogVersion    = "3.9.2"
    val pureConfigVersion  = "0.10.2"
    val zioVersion         = "1.0-RC4"
    val scalaTestVersion   = "3.0.5"
    val flywayVersion      = "5.2.4"
    val refindedVersion    = "0.9.8"
    val profigVersion      = "2.3.6"
    val scala4jLogging     = "1.7.26"
    val circeConfigVersion = "0.6.1"
  }

  def apply(): Seq[ModuleID] = Seq(
    // Project dependencies
    "com.outr"                   %% "profig"                  % Version.profigVersion,
    "de.mkammerer"               % "argon2-jvm"               % Version.argonPass,
    "co.fs2"                     %% "fs2-core"                % Version.fS2Version,
    "co.fs2"                     %% "fs2-io"                  % Version.fS2Version,
    "org.http4s"                 %% "http4s-blaze-server"     % Version.http4sVersion,
    "org.http4s"                 %% "http4s-blaze-client"     % Version.http4sVersion,
    "org.http4s"                 %% "http4s-circe"            % Version.http4sVersion,
    "org.http4s"                 %% "http4s-dsl"              % Version.http4sVersion,
    "io.circe"                   %% "circe-generic"           % Version.circeVersion,
    "io.circe"                   %% "circe-core"              % Version.circeVersion,
    "io.circe"                   %% "circe-parser"            % Version.circeVersion,
    "io.circe"                   %% "circe-generic-extras"    % Version.circeVersion,
    "io.circe"                   %% "circe-jawn"              % Version.circeVersion,
    "io.circe"                   %% "circe-config"            % Version.circeConfigVersion,
    "org.tpolecat"               %% "doobie-core"             % Version.doobieVersion,
    "org.tpolecat"               %% "doobie-hikari"           % Version.doobieVersion,
    "org.tpolecat"               %% "doobie-postgres"         % Version.doobieVersion,
    "org.tpolecat"               %% "doobie-scalatest"        % Version.doobieVersion,
    "org.flywaydb"               % "flyway-core"              % Version.flywayVersion,
    "com.pauldijou"              %% "jwt-core"                % Version.JwtVersion,
    "org.slf4j"                  % "slf4j-log4j12"            % Version.scala4jLogging,
    "com.typesafe.scala-logging" %% "scala-logging"           % Version.scalaLogVersion,
    "com.github.pureconfig"      %% "pureconfig"              % Version.pureConfigVersion,
    "com.github.pureconfig"      %% "pureconfig-cats-effect"  % Version.pureConfigVersion,
    "eu.timepit"                 %% "refined"                 % Version.refindedVersion,
    "eu.timepit"                 %% "refined-cats"            % Version.refindedVersion,
    "org.scalaz"                 %% "scalaz-zio"              % Version.zioVersion,
    "org.scalaz"                 %% "scalaz-zio-interop-cats" % Version.zioVersion,
    "org.scalatest"              %% "scalatest"               % Version.scalaTestVersion % "it, test",
    // Compiler Plugins
    compilerPlugin("org.spire-math" %% "kind-projector"     % "0.9.4"),
    compilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.3.0-M4")
  )
}
