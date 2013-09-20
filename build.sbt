name := "sample-akka-project"

version := "0.0.1"

organization := "janrain.sample"

scalaVersion := "2.10.2"

sbtVersion := "0.13.3"

resolvers ++= Seq(
  "spray" at "http://repo.spray.io/",
  "Spray Nightlies" at "http://nightlies.spray.io/",
  "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype OSS Releases"  at "http://oss.sonatype.org/content/repositories/releases/"
)

libraryDependencies ++= Seq(
  "io.spray" % "spray-httpx" % "1.2-20130712",
  "io.spray" %% "spray-json" % "1.2.5",
  "io.spray" % "spray-routing" % "1.2-20130712",
  "com.typesafe.akka" %% "akka-actor" % "2.2.0",
  "com.typesafe.akka" %% "akka-slf4j" % "2.2.0",
  "com.typesafe.slick" %% "slick" % "1.0.1",
  "com.h2database" % "h2" % "1.3.166",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "com.github.tototoshi" %% "slick-joda-mapper" % "0.3.0",
  "ch.qos.logback" % "logback-classic" % "1.0.0"
)