package mezmer.server

/*import spray.http._
import MediaTypes._
import HttpCharsets._

trait RiakService extends WebService {
  import mezmer.utils.{ User, RiakSupport => Riak }

  val riakRoutes = respondWithMediaType(MediaTypes.`application/json`) {
    pathPrefix("user" / IntNumber) { userId =>
      get { ctx =>
        val user = Riak.fetchById(userId)
        val res = HttpEntity(
          contentType = ContentType(`application/json`, `UTF-8`), 
          string = user
        )
        ctx.complete(res)
      }
    } ~
    path("users") {
      post {
        formFields('id, 'username, 'password) { (id, username, password) =>
          val newUser = new User(id.toInt, username, password)
          val riakResponse = Riak.storeUser(newUser)
          val res = HttpEntity(
            contentType = ContentType(`application/json`),
            string = riakResponse
          )
          complete(res)
        }
      }
    }
  }
}*/