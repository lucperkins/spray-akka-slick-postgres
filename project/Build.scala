import sbt._
import Keys._

object BuildSettings {
  val buildOrganization = "mezmer"
  val buildVersion      = "0.1.0-SNAPSHOT"
  val buildScalaVersion = "2.10.2"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := buildOrganization,
    version      := buildVersion,
    scalaVersion := buildScalaVersion,
    shellPrompt  := ShellPrompt.buildShellPrompt
  )
}

object ShellPrompt {
  object devnull extends ProcessLogger {
    def info (s: => String) {}
    def error (s: => String) { }
    def buffer[T] (f: => T): T = f
  }
  def currBranch = (
    ("git status -sb" lines_! devnull headOption)
      getOrElse "-" stripPrefix "## "
  )

  val buildShellPrompt = {
    (state: State) => {
      val currProject = Project.extract (state).currentProject.id
      "%s:%s:%s> ".format (
        currProject, currBranch, BuildSettings.buildVersion
      )
    }
  }
}

object Resolvers {
  val sprayRepo       = "spray"                  at "http://repo.spray.io/"
  val sprayNightlies  = "Spray Nightlies"        at "http://nightlies.spray.io/"
  val sonatypeRel     = "Sonatype OSS Releases"  at "http://oss.sonatype.org/content/repositories/releases/"
  val sonatypeSnap    = "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
  val raikuRepo       = "gideondk-repo"          at "https://raw.github.com/gideondk/gideondk-mvn-repo/master"  

  val sprayResolvers    = Seq(sprayRepo, sprayNightlies)
  val sonatypeResolvers = Seq(sonatypeRel, sonatypeSnap)
  val otherResolvers    = Seq(raikuRepo)
  val allResolvers      = sprayResolvers ++ sonatypeResolvers ++ otherResolvers
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
  val awsJavaSdk   = "com.amazonaws"        %  "aws-java-sdk"      % "1.0.002"
  val redisClient  = "net.debasishg"        %% "redisclient"       % "2.10"
  val raiku        = "nl.gideondk"          %% "raiku"             % "0.6.1"
}

object MezmerBuild extends Build {
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
    logback,
    awsJavaSdk,
    redisClient,
    raiku
  )

  val allDeps = akkaDeps ++ sprayDeps ++ otherDeps

  lazy val mainProject = Project(
    "mezmerMain",
    file("."),
    settings = buildSettings ++ Seq(resolvers           ++= allResolvers,
                                    libraryDependencies ++= allDeps)
  ) // aggregate
    // dependsOn
    
}