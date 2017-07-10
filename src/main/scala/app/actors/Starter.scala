package app.actors

import akka.actor._
import akka.io.IO
import akka.routing.RoundRobinRouter
import app.models.TaskDAO
import spray.can.Http
import app.{Configs => C}
import app.server.ServerSupervisor

object Starter {
  case object Start
  case object Stop

  def apply(taskDAO: TaskDAO) = Props(new Starter(taskDAO))
}

final class Starter(taskDAO: TaskDAO) extends Actor {
  import Starter.Start

  private implicit val system = context.system

  def receive: Receive = {
    case Start =>
      val mainHandler: ActorRef =
        context.actorOf(ServerSupervisor.apply(taskDAO).withRouter(RoundRobinRouter(nrOfInstances = 10)))
      IO(Http) ! Http.Bind(mainHandler, interface = C.interface, port = C.appPort)
  }
}