package mezmer.server

import akka.actor._

class ServerSupervisor extends Actor
	with TaskService
  with UserService
{
  def actorRefFactory = context
  def receive = runRoute(
    taskServiceRoutes ~
    userServiceRoutes
  )
}