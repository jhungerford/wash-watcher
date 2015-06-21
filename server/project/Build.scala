import sbt._
import sbt.Keys._

object WashWatcherBuild extends Build {

  scalaVersion := "2.10.4"

  lazy val washWatcher = Project(
    id = "WashWatcher",
    base = file("."))
    .settings(
      name := "WashWatcher",
      version := "0.0.1",
      parallelExecution in Test := false)
}
