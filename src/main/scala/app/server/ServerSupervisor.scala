package app.server

import akka.actor._

class ServerSupervisor extends Actor
  with TaskService
{
  def actorRefFactory = context
  def receive = runRoute(
    pathPrefix("api" / "v1") {
      taskServiceRoutes
    }
  )
}