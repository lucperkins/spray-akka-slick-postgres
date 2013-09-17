package mezmer.utils

/*
// For error handling
import scala.util.{ Success, Failure }

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
  val (riakHost, riakPort, riakWorkers) = (
    config.getString("riak.host"),
    config.getInt("riak.port"),
    config.getInt("riak.workers")
  )

  implicit val raikuSystem = ActorSystem("raikuSystem")
	val raikuClient = RaikuClient(riakHost, riakPort, riakWorkers)
	
  implicit val userFormat = jsonFormat3(User)

  implicit val userConverter = RaikuConverter.newConverter(
    reader = (v: RaikuRWValue) => userFormat.read(new String(v.data).asJson),
    writer = (u: User) => RaikuRWValue(u.id.toString, u.toJson.toString.getBytes, "application/json"),
    binIndexes = (u: User) => Map("id" -> Set(u.id.toString)),
    intIndexes = (u: User) => Map("id" -> Set(u.id.toInt))
  )

  val userBucket = RaikuBucket[User]("users", raikuClient)

  def storeUser(u: User) = {
    userBucket.store(u).start map {
      case Success(v) => v.flatMap(_.value)
      case Failure(e) => throw e
    }
  }

  def fetchUserById(id: Int) = {
    val fetchAttempt = userBucket.fetch(id.toString).start map {
      case Success(v) => v.flatMap(_.value)
      case Failure(e) => throw e
    }
    fetchAttempt.onSuccess {
      case Some(v) => s"${v.id}"
      case None    => "NONE"
    }
  }

  /*
  def fetchUserById(id: Int) = {
    val idAsString = id.toString
    val f: Future[User] = future {
      userBucket ? idAsString
    }
    f.onComplete {
      case Success(Some(u)) => u.value
      case Success(None)    => "No user found with that ID"
      case Failure(e)       => e.getMessage
    }
  }
  */

  def fetchUser(userId: Int) = {
    val userIdAsString = userId.toString
    (RiakSupport.userBucket ? userIdAsString).start.value match {
      case Some(Success(user)) => user
      case Some(Failure(_))    => "No user found for this ID"
      case _                   => "Something went wrong"
    }
  }
}*/