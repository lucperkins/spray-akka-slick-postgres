package mezmer.utils

/*
// For error handling
import scala.util.{ Try, Success, Failure }

// For dealing with futures
import scala.concurrent._
import ExecutionContext.Implicits.global
import com.typesafe.config.ConfigFactory

// From within Mezmer
// import mezmer.models.TweetJsonProtocol._

case class User(id: Int, username: String, password: String)

object RiakSupport {
  import spray.json._
  import DefaultJsonProtocol._
  import akka.actor.ActorSystem
	import nl.gideondk.raiku._

  val config = ConfigFactory.load()
  val (riakHost, riakPort) = (
    Try(config.getString("riak.host")).getOrElse("localhost"),
    Try(config.getInt("riak.port")).getOrElse(8087)
  )

  implicit val raikuSystem = ActorSystem("raikuSystem")
	val raikuClient = RaikuClient(riakHost, riakPort, 4)
	
  implicit val userFormat = jsonFormat3(User)

  implicit val userConverter = RaikuConverter.newConverter(
    reader = (v: RaikuRWValue) => userFormat.read(new String(v.data).asJson),
    writer = (u: User) => RaikuRWValue(u.id.toString, u.toJson.toString.getBytes, "application/json"),
    binIndexes = (u: User) => Map("id" -> Set(u.id.toString)),
    intIndexes = (u: User) => Map("id" -> Set(u.id.toInt))
  )

  val userBucket = RaikuBucket[User]("users", raikuClient)

  def fetchById(id: Int) = {
    val f = userBucket.fetch(id.toString).start map {
      case Success(raikuValue) => raikuValue.flatMap(_.value)
      case Failure(error) => throw error
    }
  }

  def storeUser(u: User): String = {
    (userBucket.store(u).start map {
      case Success(raikuValue) => raikuValue.flatMap(_.value)
      case Failure(error) => throw error
    }).value match {
      case Some(Success(u))    => Map("result" -> u).toJson.compactPrint
      case Some(Failure(e))    => Map("result" -> e.getMessage).toJson.compactPrint
      case None                => Map("result" -> "no user stored").toJson.compactPrint
    }
  }

  def deleteById(id: Int) = {
    (userBucket.deleteByKey(id.toString).start map {
      case Success(_) => "success"
      case Failure(e) => throw e 
    }).value match {
      case Some(_) => Map("result" -> "success").toJson.compactPrint
      case None    => Map("result" -> "none deleted").toJson.compactPrint
    }
  }
}*/