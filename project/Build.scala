import sbt._
import Keys._

object BuildSettings {
  val buildOrganization = "spray-akka-slick-postgres"
  val buildVersion      = "0.1.0"
  val buildScalaVersion = "2.10.2"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := buildOrganization,
    version      := buildVersion,
    scalaVersion := buildScalaVersion
  )

  scalacOptions ++= Seq(
    "-unchecked",
    "-deprecation",
    "-feature",
    "-encoding", "utf8"
  )
}

object Resolvers {
  val sprayRepo       = "spray"                  at "http://repo.spray.io/"
  val sprayNightlies  = "Spray Nightlies"        at "http://nightlies.spray.io/"
  val sonatypeRel     = "Sonatype OSS Releases"  at "http://oss.sonatype.org/content/repositories/releases/"
  val sonatypeSnap    = "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/" 

  val sprayResolvers    = Seq(sprayRepo, sprayNightlies)
  val sonatypeResolvers = Seq(sonatypeRel, sonatypeSnap)
  val allResolvers      = sprayResolvers ++ sonatypeResolvers
}

object Dependencies {
  val akkaVersion  = "2.2.0"
  val sprayVersion = "1.2-20130710"

  val akkaActor    = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  val akkaSlf4j    = "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
  val sprayCan     = "io.spray" %  "spray-can"     % sprayVersion
  val sprayHttpx   = "io.spray" %  "spray-httpx"   % sprayVersion
  val sprayRouting = "io.spray" %  "spray-routing" % sprayVersion
  val sprayJson    = "io.spray" %% "spray-json"    % "1.2.5"
  val slick        = "com.typesafe.slick"   %% "slick"             % "1.0.1"
  val postgres     = "postgresql"           %  "postgresql"        % "9.1-901-1.jdbc4"
  val slickJoda    = "com.github.tototoshi" %% "slick-joda-mapper" % "0.3.0"
  val scalaCsv     = "com.github.tototoshi" %% "scala-csv"         % "1.0.0-SNAPSHOT"
  val logback      = "ch.qos.logback"       %  "logback-classic"   % "1.0.0"
}

object AppBuild extends Build {
  import Resolvers._
  import Dependencies._
  import BuildSettings._

  val akkaDeps = Seq(akkaActor, akkaSlf4j)

  val sprayDeps = Seq(
    sprayCan,
    sprayHttpx,
    sprayRouting,
    sprayJson
  )

  val otherDeps = Seq(
    slick,
    postgres,
    slickJoda,
    scalaCsv,
    logback
  )

  val allDeps = akkaDeps ++ sprayDeps ++ otherDeps

  lazy val mainProject = Project(
    "spray-akka-slick-postgres",
    file("."),
    settings = buildSettings ++ Seq(resolvers           ++= allResolvers,
                                    libraryDependencies ++= allDeps)
  )
}
