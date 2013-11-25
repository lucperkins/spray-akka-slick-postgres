package app.actors

import akka.actor._
import akka.io.IO
import akka.routing.RoundRobinRouter
import spray.can.Http

import app.{ Configs => C }
import app.server.ServerSupervisor

object Starter {
  case object Start
  case object Stop
}

class Starter extends Actor {
  import Starter.{ Start, Stop }

  implicit val system = context.system

  def receive: Receive = {
    case Start =>
      val mainHandler: ActorRef =
        context.actorOf(Props[ServerSupervisor].withRouter(RoundRobinRouter(nrOfInstances = 10)))
      IO(Http) ! Http.Bind(mainHandler, interface = C.interface, port = C.appPort)
  }
}