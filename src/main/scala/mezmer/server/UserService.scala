package mezmer.server

import nl.gideondk.raiku.{ RaikuValue }
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }
import spray.json._
import DefaultJsonProtocol._

import mezmer.models.{ User, UserDAO }
import mezmer.utils.RiakSupport

trait UserService extends WebService {
  import spray.httpx.SprayJsonSupport._

  val bucket = UserDAO.userBucket

  val userServiceRoutes = {
    path("user" / IntNumber) { userId =>
      get { ctx =>
        val f = UserDAO.fetchUserById(userId)
        ctx.complete(f)
      } /*~
        put {
          formFields('name.as[String]) { (name) =>
            val f = UserDAO.modifyUserById(userId)
            complete(f)
          }
        }*/
    } ~
    path("users") {
      post {
        formFields('id.as[Int], 'name.as[String]) { (id, name) =>
          val newUser = new User(id, name, List())
          val f = UserDAO.storeNewUser(id, name)
          complete(f)
        }
      }
    }
  }
}