package mezmer.server

import akka.actor.{ ActorSystem, Props }
import spray.routing.HttpService
import akka.event.Logging
import mezmer.actors.PostgresWorker

trait WebService extends HttpService {
  implicit def executionContext = actorRefFactory.dispatcher
}