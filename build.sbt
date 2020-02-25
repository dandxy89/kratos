organization := "com.dandxy89"
scalaVersion := "2.13.1"
version := Version()
name := "kratos-backend"

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
