package mezmer.server

import akka.actor._
import spray.routing.HttpService

trait WebService extends HttpService {
  implicit val executionContext = actorRefFactory.dispatcher
}