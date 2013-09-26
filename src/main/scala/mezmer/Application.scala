package mezmer

import akka.actor._
import scala.slick.jdbc.meta.MTable
import slick.driver.PostgresDriver.simple._
import sys.process._

import mezmer.actors.Starter
import mezmer.utils.PostgresSupport
import mezmer.models.TaskDAO

object Application extends App
  with PostgresSupport
{
  val system = ActorSystem("main-system")

  if (MTable.getTables("tasks").list.isEmpty) {
    TaskDAO.createTable
  } else {
    println("Postgres is ready")
  }

  "riak ping".! match {
    case 1 => {
      // log.info("Riak is not connected")
      "riak start".!
      // log.info("Riak is connecting....")
      // log.info("Riak is now connected!")
    }
    case _ => "Something went wrong" //log.info("Riak is connected")
  }

  val starter = system.actorOf(Props[Starter], name = "main")

  starter ! Starter.Start
}