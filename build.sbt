organization := "com.dandxy89"
scalaVersion := "2.12.9"
version := Version()
name := "StrokesGained"

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    libraryDependencies ++= ProjectDependencies(),
    ScalacOptions.settings
  )

Test / fork := false
Test / testForkedParallel := false
IntegrationTest / fork := false
IntegrationTest / testForkedParallel := false

resolvers += Resolver.mavenLocal
