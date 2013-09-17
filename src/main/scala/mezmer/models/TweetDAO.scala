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
  lastModified: DateTime,
  content:      String,
  retweeted:    Boolean,
  username:     String
)

object TweetDAO extends PostgresSupport 
  // with RedisSupport
{
  import mezmer.models.TweetJsonProtocol._

  object TweetTable extends Table[Tweet]("tweets") {
    def tweetId      = column[Int]     ("tweetId", O.AutoInc, O.PrimaryKey, O.DBType("BIGINT"))
    def created      = column[DateTime] ("created", O.DBType("TIMESTAMP"))
    def lastModified = column[DateTime] ("lastmodified", O.DBType("TIMESTAMP"))
    def content      = column[String]   ("content", O.DBType("VARCHAR(140)"))
    def retweeted    = column[Boolean]  ("retweeted", O.DBType("BOOLEAN"))
    def username     = column[String]   ("username", O.DBType("VARCHAR(12)"))

    def *            = (tweetId ~ created ~ lastModified ~ content ~ retweeted ~ username) <> (Tweet, Tweet.unapply _)

    def forInsert    = (created ~ lastModified ~ content ~ retweeted ~ username) returning tweetId
  }

  /*
  implicit val dateTypeMapper = MappedTypeMapper.base[java.util.Date, java.sql.Date] (
    { ud => new java.sql.Date(ud.getTime) },
    { sd => new java.util.Date(sd.getTime) }
  )
  */

  def listAllTweets =
    Query(TweetTable).list.toJson

  def createTable =
    TweetTable.ddl.create

  def dropTable =
    TweetTable.ddl.drop

  def addTweet(username: String, content: String) = {
    val now = new DateTime()
    val retweeted = false
    TweetTable.forInsert.insert(now, now, content, retweeted, username) match {
      case 0 => "Something went wrong"
      case n => s"Tweet number {n} added successfully"
    }
  }

  def fetchTweetById(id: Int) = {
    Query(TweetTable).where(_.tweetId is id).list.toJson
  }

  def deleteTweetById(id: Int) = {
    TweetTable.filter(_.tweetId === id).delete match {
      case 0 => "0 tweets deleted"
      case 1 => "1 tweet successfully deleted"
      case _ => "Something went wrong"
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