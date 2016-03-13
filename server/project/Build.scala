import sbt._
import sbt.Keys._

object WashWatcherBuild extends Build {
  lazy val scalatraVersion = "2.3.1"

  lazy val root = project.in(file(".")).aggregate(server, interface)

  lazy val commonSettings = Seq(
    organization := "dev",
    version := "0.0.1",
    scalaVersion := "2.11.8"
  )

  lazy val finatraVersion = "2.1.4"

  lazy val server = project
    .in(file("washwatcher-server"))
    .settings(commonSettings: _*)
    .settings(
      name := "washwatcher-server",
      resolvers += "Twitter Maven" at "http://maven.twttr.com",
      libraryDependencies ++= Seq(
        "com.twitter.finatra" %% "finatra-http" % finatraVersion,
        "org.apache.commons" % "commons-math3" % "3.5"
      )
    )

  lazy val interface = project
    .in(file("washwatcher-interface"))
    .settings(commonSettings: _*)
    .settings(
      name := "washwatcher-interface"
    )
}
