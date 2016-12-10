name := "BigData"

version := "1.0"

scalaVersion := "2.10.6"


libraryDependencies ++= Seq("org.apache.spark" %% "spark-core" % "2.0.0",
  "joda-time" % "joda-time" % "2.9.6",
  "org.apache.spark" %% "spark-mllib" % "2.0.0",
  "com.google.maps" % "google-maps-services" % "0.1.17",
  "org.json4s" %% "json4s-native" % "3.2.11",
  "org.mongodb" %% "casbah" % "2.8.1")

//resolvers += "Akka Repository" at "http://repo.akka.io/releases/"
//resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"