package app.server

import akka.actor._
import akka.util.Timeout
import scala.concurrent.duration._
import spray.routing.HttpService

trait WebService extends HttpService {
  implicit def executionContext = actorRefFactory.dispatcher
  implicit val timeout = Timeout(120 seconds)
}