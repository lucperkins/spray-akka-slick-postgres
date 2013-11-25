package app

import akka.actor._

import app.{ Configs => C }
import app.actors.Starter
import app.utils.PostgresSupport

object Application extends App
  with PostgresSupport
{
  val system = ActorSystem("main-system")
  C.log.info("Actor system $system is up and running")

  startPostgres()
  C.log.info("Postgres is up and running")

  val starter = system.actorOf(Props[Starter], name = "main")

  starter ! Starter.Start
}