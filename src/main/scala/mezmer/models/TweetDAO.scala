package mezmer.models

import spray.json._
import DefaultJsonProtocol._
import scala.slick.driver.PostgresDriver.simple._
import com.github.tototoshi.slick.JodaSupport._
import org.joda.time.DateTime
import scala.slick.driver.PostgresDriver
// import com.github.tminglei.slickpg._
// import com.github.tototoshi.csv._

import mezmer.utils.PostgresSupport
// import mezmer.utils.RedisSupport

case class Tweet(
  tweetId:      Int,
  created:      DateTime,
  content:      String,
  retweeted:    Boolean,
  username:     String
)

object TweetDAO extends PostgresSupport 
  // with RedisSupport
{
  import mezmer.data.TweetJsonProtocol._

  object TweetTable extends Table[Tweet]("tweets") {
    def tweetId      = column[Int]     ("tweetId", O.AutoInc, O.PrimaryKey, O.DBType("BIGINT"))
    def created      = column[DateTime] ("created", O.DBType("TIMESTAMP"))
    def content      = column[String]   ("content", O.DBType("VARCHAR(140)"))
    def retweeted    = column[Boolean]  ("retweeted", O.DBType("BOOLEAN"))
    def username     = column[String]   ("username", O.DBType("VARCHAR(20)"))

    def *            = (tweetId ~ created ~ content ~ retweeted ~ username) <> (Tweet, Tweet.unapply _)

    def forInsert    = (created ~ content ~ retweeted ~ username) returning tweetId
  }

  def listAllTweets: String =
    Query(TweetTable).list.toJson.prettyPrint

  // Convert count to JSON

  case class Count(total: Int)
  implicit val countJsonFormat = jsonFormat1(Count)

  def numberOfTweets: String = Query(TweetTable).list.length.toJson.prettyPrint

  def createTable =
    TweetTable.ddl.create

  def dropTable =
    TweetTable.ddl.drop

  def addTweet(username: String, content: String): String = {
    val now = new DateTime()
    val retweeted = false
    TweetTable.forInsert.insert(now, content, retweeted, username) match {
      case 0 => "Something went wrong"
      case n => s"Tweet $n added successfully"
    }
  }

  def fetchTweetById(id: Int): String = 
    Query(TweetTable).where(_.tweetId is id).list.toJson.prettyPrint

  def deleteTweetById(id: Int): String = {
    TweetTable.filter(_.tweetId === id).delete match {
      case 0 => s"Tweet $id was not found"
      case 1 => s"Tweet $id successfully deleted"
      case _ => "Something went wrong"
    }
  }

  def updateTweetById(id: Int, newContent: String): String = {
    TweetTable.where(_.tweetId is id).
      map(t => t.content).
      update(newContent) match {
        case 1 => s"Tweet $id successfully modified"
        case _ => s"Tweet $id was not found"
      }
  }

  // Redis stuff
  /*val R = RedisSupport

  def storeTweetInRedis(id: Int) = {
    R.redisClient(r).set("id", id)
  }

  def fetchFromRedisById(id: Int) = {
    R.redisClient(r).get(id)
  }*/
}