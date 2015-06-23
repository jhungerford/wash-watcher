scalaVersion := "2.10.4"

val scalatraVersion = "2.3.1"

resolvers += Resolver.sonatypeRepo("public")

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % scalatraVersion,
  "org.scalatra" %% "scalatra-json" % scalatraVersion,
  "org.json4s"   %% "json4s-jackson" % "3.2.6",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "org.eclipse.jetty" % "jetty-webapp" % "9.3.0.v20150612",
  "org.scalatra" %% "scalatra-specs2" % scalatraVersion % "test",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)
