package mezmer

import akka.actor.{ ActorSystem, Props }
import mezmer.actors.Starter
import mezmer.utils.PostgresSupport
import mezmer.models.TweetDAO
import slick.driver.PostgresDriver.simple._
import scala.slick.jdbc.meta.MTable

object Application extends App with PostgresSupport {
  // db.withSession {
    if (MTable.getTables("tweets").list.isEmpty) {
      TweetDAO.createTable
    } else {
      println("Postgres is ready")
    }
  // }

  val system = ActorSystem("mezmer")
  val starter = system.actorOf(Props[Starter], name = "main")
  starter ! Starter.Start
}