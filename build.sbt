name := "mezmer"

version := "0.0.1"

organization := "janrain.sample"

scalaVersion := "2.10.2"

sbtVersion := "0.12.3"

resolvers ++= Seq(
  "spray" at "http://repo.spray.io/",
  "Spray Nightlies" at "http://nightlies.spray.io/",
  "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/",
  "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
  "gideondk-repo" at "https://raw.github.com/gideondk/gideondk-mvn-repo/master"
)

libraryDependencies ++= Seq(
  "io.spray" % "spray-can" % "1.2-20130710",
  "io.spray" % "spray-httpx" % "1.2-20130710",
  "io.spray" %% "spray-json" % "1.2.5",
  "io.spray" % "spray-routing" % "1.2-20130710",
  "com.typesafe.akka" %% "akka-actor" % "2.2.0",
  "com.typesafe.akka" %% "akka-slf4j" % "2.2.0",
  "com.typesafe.slick" %% "slick" % "1.0.1",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "com.github.tototoshi" %% "slick-joda-mapper" % "0.3.0",
  "com.github.tototoshi" %% "scala-csv" % "1.0.0-SNAPSHOT",
  "ch.qos.logback" % "logback-classic" % "1.0.0",
  "com.amazonaws" % "aws-java-sdk" % "1.0.002",
  "net.debasishg" %% "redisclient" % "2.10",
  "nl.gideondk" %% "raiku" % "0.6.1"
)