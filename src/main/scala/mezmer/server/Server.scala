package mezmer.server

import akka.actor._
import spray.http._
import HttpCharsets._
import MediaTypes._

class ServerSupervisor extends Actor
  with TweetService
  with S3Service
  with RiakService
{
  def actorRefFactory = context
  def receive = runRoute(restRoutes ~ s3Routes/* ~ riakRoutes*/)
}

/*
(bucket.store(luc).start map {
	case Success(Some(user)) => user.value
	case _ => "OOPS"
}).value match {
	
}

*/