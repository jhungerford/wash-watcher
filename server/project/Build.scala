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
      resolvers += "Twitter Maven" at "http://maven.twttr.com",
      libraryDependencies ++= Seq(
        "org.apache.thrift" % "libthrift" % "0.9.2",
        "com.twitter.finatra" %% "finatra-http" % "2.0.0.M2",
        "com.twitter.finatra" %% "finatra-httpclient" % "2.0.0.M2",
        "com.twitter.finatra" %% "finatra-logback" % "2.0.0.M2"
      )
    )

  lazy val interface = project
    .in(file("washwatcher-interface"))
    .settings(commonSettings: _*)
    .settings(
      name := "washwatcher-interface"
    )
}
