package mezmer.server

import spray.http._
import MediaTypes._
import HttpCharsets._
import spray.json._

import mezmer.models.TweetDAO
// import mezmer.data.TweetJsonProtocol._

trait TweetService extends WebService {
  val tweetServiceRoutes = {
    path("count") {
      get {
        val numberOfTweets: String = TweetDAO.numberOfTweets
        val res = HttpEntity(
          contentType = ContentType(`application/json`),
          string = numberOfTweets
        )
        complete(res)
      }
    } ~
    path("tweets") {
      get { ctx =>
        val allTweets: String = TweetDAO.listAllTweets
        val res = HttpEntity(
          contentType = ContentType(`application/json`, `UTF-8`),
          string = allTweets
        )
        ctx.complete(res)
      } ~
        post {
          formFields('content, 'username) { (content, username) =>
            val pgResponse = TweetDAO.addTweet(content, username)
            val res = HttpEntity(
              contentType = ContentType(`text/plain`),
              string = pgResponse
            )
            complete(res)
          }
        }
    } ~
    pathPrefix("tweet" / IntNumber) { tweetId =>
      get { ctx =>
        val tweet: String = TweetDAO.fetchTweetById(tweetId)
        val res = HttpEntity(
          contentType = ContentType(`application/json`, `UTF-8`),
          string = tweet
        )
        ctx.complete(res)
      } ~
        delete { ctx =>
          val pgResponse = TweetDAO.deleteTweetById(tweetId)
          val res = HttpEntity(
            contentType = ContentType(`text/plain`),
            string = pgResponse
          )
          ctx.complete(res)
        } ~
          put {
            formFields('content) { content =>
              val pgResponse = TweetDAO.updateTweetById(tweetId, content)
              val res = HttpEntity(
                contentType = ContentType(`text/plain`),
                string = pgResponse
              )
              complete(res)
            }
          }
    } ~
    path("api") {
      get {
        complete("\n  HTTP methods:\n  =============\n\n  GET /count\n  GET /tweets\n  GET /tweet/:id\n  PUT /tweet/:id\n  DELETE /tweet/:id\n  POST /tweets\n\n\n")
      }
    }
  }
}