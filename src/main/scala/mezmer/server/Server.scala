package mezmer.server

import akka.actor._
import spray.routing.{HttpService, RequestContext}
import spray.can.Http
import spray.util._
import spray.http._
import HttpCharsets._
import MediaTypes._
import spray.httpx.SprayJsonSupport._
import org.slf4j.LoggerFactory
import ch.qos.logback.core.util.StatusPrinter
import ch.qos.logback.classic.LoggerContext

import mezmer.models.TweetDAO
import mezmer.server.WebService

class Server extends Actor
  with UserService
  with TweetService
{
  def actorRefFactory = context
  def receive = runRoute(restRoutes ~ userRoutes)
}

trait UserService extends WebService {
  val userRoutes = {
    path("users") {
      get {
        complete("user route")
      }
    }
  }
}

trait TweetService extends WebService {
  import mezmer.data.TweetJsonProtocol._
  import mezmer.actors.WebWorker._
  import mezmer.actors.WebWorker
  def logger = LoggerFactory.getLogger(this.getClass)

  val tweetServiceSystem = ActorSystem("tweet-service-system")
  val worker = tweetServiceSystem.actorOf(Props[WebWorker], "web-worker")

  val restRoutes = {
    path("tweets") {
      get {
        val allTweets = TweetDAO.listAllTweets
        var res = HttpEntity(
          contentType = ContentType(`application/json`, `UTF-8`),
          string = allTweets.toString
        )
        logger.info(s"")
        _.complete(res)
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
          _.complete(res)
        } ~
        delete {
          val res = TweetDAO.deleteTweetById(tweetId)
          _.complete(res)
        }
      }
    } ~
    path("worker") { ctx =>
      get {
        worker ! Go
        complete("OK")
      }
    }
  }
}