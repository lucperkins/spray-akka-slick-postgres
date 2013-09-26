package mezmer.server

import akka.actor._
import akka.util.Timeout
import scala.concurrent.duration._
import spray.routing.HttpService

trait WebService extends HttpService {
  implicit val timeout = Timeout(5 seconds)
}