package mezmer

import akka.actor.{ ActorSystem, Props }
import mezmer.actors.Starter

object Application extends App {
  val system = ActorSystem("mezmer")
  val starter = system.actorOf(Props[Starter], name = "main")
  starter ! Starter.Start
}