package mezmer

import akka.actor.{ ActorSystem, Props }
import mezmer.actors.Starter
import mezmer.utils.PostgresSupport
import mezmer.models.TaskDAO
import slick.driver.PostgresDriver.simple._
import scala.slick.jdbc.meta.MTable

object Application extends App with PostgresSupport {
  if (MTable.getTables("tasks").list.isEmpty) {
      TaskDAO.createTable
    } else {
      println("Postgres is ready")
    }

  val system = ActorSystem("mezmer")
  val starter = system.actorOf(Props[Starter], name = "main")
  starter ! Starter.Start
}