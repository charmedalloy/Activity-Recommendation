name := "Activity-Recommendation"

version := "2.0"

scalaVersion := "2.10.6"

val sprayVersion = "1.2-M8"
val akkaVersion = "2.2.0-RC1"

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/"
)

libraryDependencies ++= Seq("org.apache.spark" %% "spark-core" % "2.0.0",
  "joda-time" % "joda-time" % "2.9.6",
  "org.apache.spark" %% "spark-mllib" % "2.0.0",
  "com.google.maps" % "google-maps-services" % "0.1.17",
  "org.json4s" %% "json4s-native" % "3.2.11",
  "org.mongodb" %% "casbah" % "2.8.1",
  "io.spray" % "spray-can" % sprayVersion,
  "io.spray" % "spray-routing" % sprayVersion,
  "io.spray" % "spray-testkit" % sprayVersion,
  "io.spray" % "spray-client" % sprayVersion,
  "io.spray" %% "spray-json" % "1.2.5",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.0.12",
  "org.scalatest" %% "scalatest" % "2.0.M7" % "test"
)

seq(Revolver.settings: _*)


