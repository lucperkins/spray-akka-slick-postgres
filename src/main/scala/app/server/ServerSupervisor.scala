package app.server

import akka.actor._
import app.models.TaskDAO

object ServerSupervisor {
  def apply(taskDAO: TaskDAO) = Props(new ServerSupervisor(taskDAO))
}

final class ServerSupervisor(taskDAO: TaskDAO) extends Actor
  with TaskService {
  def actorRefFactory = context

  def receive = runRoute(
    pathPrefix("api" / "v1") {
      taskServiceRoutes(taskDAO)
    }
  )
}