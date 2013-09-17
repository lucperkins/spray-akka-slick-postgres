package mezmer.server

// import java.io.File
// import org.parboiled.common.FileUtils
// import scala.concurrent.duration.Duration
import akka.actor._
// import akka.pattern.ask
import spray.routing.{HttpService, RequestContext}
import spray.routing.directives.CachingDirectives
import spray.can.server.Stats
import spray.can.Http
import spray.httpx.unmarshalling.pimpHttpEntity
import spray.json.DefaultJsonProtocol
import spray.httpx.marshalling._
import spray.httpx.marshalling.Marshaller
import spray.httpx.encoding.Gzip
import spray.util._
import spray.http._
import HttpCharsets._
import MediaTypes._
import CachingDirectives._
import spray.httpx.SprayJsonSupport._

import mezmer.models.TweetDAO
// import mezmer.server.WebWorker._

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
  // val simpleCache = routeCache(maxCapacity = 1000, timeToIdle = Duration("30 min"))
  import mezmer.models.TweetJsonProtocol._

  implicit val executionContext = actorRefFactory.dispatcher

  // val webWorker = actorRefFactory.actorOf(Props[WebWorker], "web-worker")

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