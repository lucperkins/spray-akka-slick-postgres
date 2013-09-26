package mezmer

import akka.actor._
import scala.slick.jdbc.meta.MTable
import slick.driver.PostgresDriver.simple._

import mezmer.actors.Starter
import mezmer.utils.PostgresSupport
import mezmer.models.TaskDAO

object Application extends App with PostgresSupport {
  if (MTable.getTables("tasks").list.isEmpty) {
    TaskDAO.createTable
  } else {
    println("Postgres is ready")
  }

  val system = ActorSystem("main-system")
  val starter = system.actorOf(Props[Starter], name = "main")
  starter ! Starter.Start
}