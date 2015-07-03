import sbt._
import sbt.Keys._

object WashWatcherBuild extends Build {
  lazy val scalatraVersion = "2.3.1"

  lazy val root = project.in(file(".")).aggregate(server, interface)


  lazy val commonSettings = Seq(
    organization := "dev",
    version := "0.0.1",
    scalaVersion := "2.10.5"
  )

  lazy val server = project
    .in(file("washwatcher-server"))
    .settings(commonSettings: _*)
    .settings(
      name := "washwatcher-server",
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % scalatraVersion,
        "org.scalatra" %% "scalatra-json" % scalatraVersion,
        "org.json4s" %% "json4s-jackson" % "3.2.6",
        "ch.qos.logback" % "logback-classic" % "1.1.3",
        "org.eclipse.jetty" % "jetty-webapp" % "9.3.0.v20150612",
        "org.scalatra" %% "scalatra-specs2" % scalatraVersion % "test",
        "org.scalatest" %% "scalatest" % "2.2.1" % "test"
      )
    )

  lazy val interface = project
    .in(file("washwatcher-interface"))
    .settings(commonSettings: _*)
    .settings(
      name := "washwatcher-interface"
    )
}
