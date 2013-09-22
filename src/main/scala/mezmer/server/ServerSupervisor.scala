package mezmer.server

import akka.actor._

class ServerSupervisor extends Actor
	with TaskService
	with S3Service
	with PersonService
{
  def actorRefFactory = context
  def receive = runRoute(taskServiceRoutes ~ s3Routes ~ personRoutes)
}