package mezmer.server

import akka.actor._
import spray.http._
import HttpCharsets._
import MediaTypes._

class ServerSupervisor extends Actor with TweetService {
  def actorRefFactory = context
  def receive = runRoute(tweetServiceRoutes)
}