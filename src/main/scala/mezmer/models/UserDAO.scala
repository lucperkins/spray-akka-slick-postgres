package mezmer.models

import nl.gideondk.raiku.{ RaikuBucket, RaikuConverter, RaikuRWValue }
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{ Failure, Success }
import spray.json._
import DefaultJsonProtocol._

import mezmer.utils.RiakSupport

case class User(
  id: Int,
  name: String,
  tasks: List[Task]
)

object UserJsonProtocol extends DefaultJsonProtocol {
  import mezmer.models.Task
  import mezmer.data.TaskJsonProtocol._
  import mezmer.models.UserJsonProtocol._

  implicit val userFormat = jsonFormat3(User)
}

object UserDAO extends RiakSupport {
  import UserJsonProtocol._

  implicit val userConverter = RaikuConverter.newConverter(
    reader = (v: RaikuRWValue) => userFormat.read(new String(v.data).asJson),
    writer = (u: User)         => RaikuRWValue(u.id.toString, u.toJson.toString.getBytes, "application/json"),
    binIndexes = (u: User)     => Map("id" -> Set(u.id.toString)),
    intIndexes = (u: User)     => Map("id" -> Set(u.id))
  )

  val userBucket = RaikuBucket[User]("users", raikuClient)

  def fetchUserById(id: Int) = (userBucket ? id.toString).start map {
    case Success(Some(u)) => u.value.toJson.compactPrint
    case Success(None)    => Map("result" -> "none").toJson.compactPrint
    case Failure(e)       => Map("error" -> e.getMessage).toJson.compactPrint
  }

  def storeNewUser(id: Int, name: String) = {
    val newUser = new User(id, name, List())
    (userBucket << newUser).start map {
      case Success(_) => Map("result" -> s"user $id stored successfully").toJson.compactPrint
      case Failure(e) => Map("error" -> e.getMessage).toJson.compactPrint
    }
  }

  def storeMultiple(us: List[User]) = {
    (userBucket <<* us).start map {
      case Success(_) => Map("result" -> "${us.length} users stored successfully").toJson.compactPrint
      case Failure(e) => Map("error" -> e.getMessage).toJson.compactPrint
    }
  }
}