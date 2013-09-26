package mezmer.actors

import akka.actor._
import akka.io.IO
import spray.can.Http
import akka.routing.RoundRobinRouter
import com.typesafe.config.ConfigFactory

import mezmer.server.ServerSupervisor

object Starter {
  case object Start
}

class Starter extends Actor {
  import Starter._

  val appConfig = ConfigFactory.load()
  lazy val (mainInterface: String, mainPort: Int) = (
    appConfig.getString("app.interface"),
    appConfig.getInt("app.port")
  )

  implicit val system = context.system

  def receive: Receive = {
    case Start =>
      val mainHandler: ActorRef =
        context.actorOf(Props[ServerSupervisor].withRouter(RoundRobinRouter(nrOfInstances = 10)))
      // val v1Handler: ActorRef =
      //   context.actorOf(Props[V1Handler].withRouter(RoundRobinRouter(nrOfInstances = 10)))
      // IO(Http) ! Http.Bind(v1Handler, interface = altInterface, port = secondaryPort)
      IO(Http) ! Http.Bind(mainHandler, interface = mainInterface, port = mainPort)
  }
}