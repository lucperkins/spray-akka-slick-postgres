package mezmer.server

import akka.actor._
import spray.routing.HttpService

abstract trait WebService extends HttpService {
  implicit val executionContext = actorRefFactory.dispatcher
}