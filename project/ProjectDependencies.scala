import sbt.{ ModuleID, compilerPlugin, _ }

object ProjectDependencies extends {

  private[this] object Version {
    val doobieVersion      = "0.8.8"
    val fS2Version         = "2.2.2"
    val scalaLogVersion    = "3.9.2"
    val pureConfigVersion  = "0.12.2"
    val zioVersion         = "1.0.0-RC17"
    val scalaTestVersion   = "3.1.1"
    val flywayVersion      = "6.2.4"
    val scala4jLogging     = "1.7.30"
    val circeConfigVersion = "0.7.0"
    val kratosLibVersion   = "0.0.1-SNAPSHOT"
  }

  def apply(): Seq[ModuleID] = Seq(
    // Project dependencies
    "co.fs2"                     %% "fs2-core"               % Version.fS2Version,
    "co.fs2"                     %% "fs2-io"                 % Version.fS2Version,
    "io.circe"                   %% "circe-config"           % Version.circeConfigVersion,
    "org.tpolecat"               %% "doobie-core"            % Version.doobieVersion,
    "org.tpolecat"               %% "doobie-hikari"          % Version.doobieVersion,
    "org.tpolecat"               %% "doobie-postgres"        % Version.doobieVersion,
    "org.tpolecat"               %% "doobie-scalatest"       % Version.doobieVersion,
    "org.flywaydb"               % "flyway-core"             % Version.flywayVersion,
    "org.slf4j"                  % "slf4j-log4j12"           % Version.scala4jLogging,
    "com.typesafe.scala-logging" %% "scala-logging"          % Version.scalaLogVersion,
    "com.github.pureconfig"      %% "pureconfig"             % Version.pureConfigVersion,
    "com.github.pureconfig"      %% "pureconfig-cats-effect" % Version.pureConfigVersion,
    "dev.zio"                    %% "zio"                    % Version.zioVersion,
    "org.scalatest"              %% "scalatest"              % Version.scalaTestVersion % "it, test",
    // Kratos Libraries
    "com.dandxy89" %% "kratos-middleware" % Version.kratosLibVersion,
    "com.dandxy89" %% "kratos-auth"       % Version.kratosLibVersion,
    // Compiler Plugins
    compilerPlugin("org.typelevel" %% "kind-projector"     % "0.11.0" cross CrossVersion.full),
    compilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
  )
}
