package mezmer.server

import akka.actor._
import spray.routing.{HttpService, RequestContext}
import spray.can.Http
import spray.util._
import spray.http._
import HttpCharsets._
import MediaTypes._
import spray.httpx.SprayJsonSupport._

import mezmer.models.TweetDAO

class Server extends Actor
  with UserService
  with TweetService
{
  def actorRefFactory = context
  def receive = runRoute(restRoutes ~ userRoutes)
}

trait UserService extends HttpService {
  val userRoutes = {
    path("users") {
      get {
        complete("user route")
      }
    }
  }
}

trait TweetService extends HttpService {
  import mezmer.data.TweetJsonProtocol._
  implicit val executionContext = actorRefFactory.dispatcher

  val restRoutes = {
    path("tweets") {
      get {
        val allTweets = TweetDAO.listAllTweets
        var res = HttpEntity(
          contentType = ContentType(`application/json`, `UTF-8`),
          string = allTweets.toString
        )
        complete(res)
      }
    } ~
    pathPrefix("tweet" / IntNumber) { tweetId =>
      path("") {
        get {
          val tweet = TweetDAO.fetchTweetById(tweetId)
          val res = HttpEntity(
            contentType = ContentType(`application/json`, `UTF-8`),
            string = tweet.toString
          )
          complete(res)
        } ~
        delete {
          val res = TweetDAO.deleteTweetById(tweetId)
          complete(res)
        }
      }
    }
  }
}