ThisBuild / organization := "com.dandxy89"
ThisBuild / scalaVersion := "2.12.8"
ThisBuild / version := Version()
ThisBuild / name := "StrokesGained"

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
