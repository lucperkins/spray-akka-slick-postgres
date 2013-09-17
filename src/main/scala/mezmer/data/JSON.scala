package mezmer.data

import spray.json._
import DefaultJsonProtocol._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

import mezmer.models.Tweet

object TweetJsonProtocol extends DefaultJsonProtocol {

  val isoParser = ISODateTimeFormat.dateTimeParser()
  def dtParse(dt: String): DateTime = isoParser.parseDateTime(dt)

	implicit object TweetJsonFormat extends RootJsonFormat[Tweet] {
		def write(t: Tweet) = JsObject(
      "tweetId"      -> JsNumber(t.tweetId.toInt),
      "created"      -> JsString(t.created.toString()),
      "lastModified" -> JsString(t.lastModified.toString()),
      "content"      -> JsString(t.content.toString),
      "retweeted"    -> JsBoolean(t.retweeted),
      "username"     -> JsString(t.username.toString)
    )
    def read(j: JsValue) = {
      j.asJsObject.getFields("tweetId",
                             "created",
                             "lastModified",
                             "content",
                             "retweeted",
                             "username"
      ) match {
        case Seq(
          JsNumber(tweetId),
          JsString(created),
          JsString(lastModified),
          JsString(content),
          JsBoolean(retweeted),
          JsString(username)
        ) => new Tweet(
          tweetId.toInt,
          dtParse(created),
          dtParse(lastModified),
          content,
          retweeted,
          username
        )
        case _ => throw new DeserializationException("Tweet expected")
      }
    }
	}
}