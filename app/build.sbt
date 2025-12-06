enablePlugins(AssemblyPlugin, ScalafmtPlugin)

ThisBuild / scalaVersion := "3.3.3"
ThisBuild / organization := "com.example"
ThisBuild / version := "0.1.0"

ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-source:future"
)

ThisBuild / libraryDependencies ++= Seq(
  "org.scalameta" %% "munit" % "1.2.1" % Test
)

ThisBuild / scalafmtOnCompile := true

Compile / run / mainClass := Some("app.Demo")
assembly / mainClass := Some("app.Demo")