name := "StrokesGained"
version := "0.1"
scalaVersion := "2.12.8"

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

libraryDependencies ++= Seq(
  "co.fs2"                     %% "fs2-core"                % FS2Version,
  "co.fs2"                     %% "fs2-io"                  % FS2Version,
  "org.http4s"                 %% "http4s-blaze-server"     % Http4sVersion,
  "org.http4s"                 %% "http4s-blaze-client"     % Http4sVersion,
  "org.http4s"                 %% "http4s-circe"            % Http4sVersion,
  "org.http4s"                 %% "http4s-dsl"              % Http4sVersion,
  "io.circe"                   %% "circe-generic"           % CirceVersion,
  "io.circe"                   %% "circe-config"            % CirceConfigVersion,
  "org.tpolecat"               %% "doobie-core"             % DoobieVersion,
  "org.tpolecat"               %% "doobie-h2"               % DoobieVersion,
  "org.tpolecat"               %% "doobie-hikari"           % DoobieVersion,
  "org.tpolecat"               %% "doobie-postgres"         % DoobieVersion,
  "com.h2database"             % "h2"                       % H2Version,
  "org.flywaydb"               % "flyway-core"              % FlywayVersion,
  "org.slf4j"                  % "slf4j-log4j12"            % Scala4jLogging,
  "com.typesafe.scala-logging" %% "scala-logging"           % ScalaLogVersion,
  "com.github.pureconfig"      %% "pureconfig"              % PureConfigVersion,
  "com.github.pureconfig"      %% "pureconfig-cats-effect"  % PureConfigVersion,
  "eu.timepit"                 %% "refined"                 % RefindedVersion,
  "eu.timepit"                 %% "refined-cats"            % RefindedVersion,
  "org.scalaz"                 %% "scalaz-zio"              % ZioVersion,
  "org.scalaz"                 %% "scalaz-zio-interop-cats" % ZioVersion,
  "org.scalatest"              %% "scalatest"               % ScalaTestVersion % "test",
  compilerPlugin("org.spire-math" %% "kind-projector"     % "0.9.4"),
  compilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.3.0-M4")
)

scalacOptions += "-Ypartial-unification"
