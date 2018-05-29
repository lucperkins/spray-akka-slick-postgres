package app

import akka.actor._
import app.{Configs => C}
import app.actors.Starter
import app.models.TaskDAO
import app.utils.PostgresSupport
import scala.concurrent.duration._

import scala.concurrent.Await

object Application extends App
  with PostgresSupport
{
  val system = ActorSystem("main-system")
  C.log.info("Actor system $system is up and running")
  C.log.info("Postgres is up and running")
  private implicit val context = system.dispatcher
  private val taskDAO = new TaskDAO()
  Await.result(startPostgres(taskDAO), 1.second)
  val starter = system.actorOf(Starter.apply(taskDAO), name = "main")

  starter ! Starter.Start
}
