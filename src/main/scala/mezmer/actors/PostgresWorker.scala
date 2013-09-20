package mezmer.actors

import akka.actor._
import mezmer.models.TweetDAO

case class Test
case class FetchTweet(id: Int)
case class FetchAll

class PostgresWorker extends Actor {
  // import PostgresWorker._

  implicit val system = context.system

  def receive: Receive = {
    case Test => s"It worked!"
    case FetchTweet(id) =>
      sender ! TweetDAO.fetchTweetById(id)
    case FetchAll =>
      sender ! TweetDAO.listAllTweets
  }
}