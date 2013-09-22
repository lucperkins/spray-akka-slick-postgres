package mezmer.server

import spray.http._

import mezmer.utils.{ RiakSupport => R }
import mezmer.server.HTTPHelpers._

trait PersonService extends WebService {
  val personRoutes = {
    path("person" / IntNumber) { personId =>
      get { ctx =>
      	ctx.complete(R.fetchPerson(personId))
      }
    } ~
    path("people") {
      post {
        formFields('id.as[Int], 'name.as[String]) { (id, name) =>
          complete(R.storePerson(new R.Person(id, name)))
        }
      }
    }
  }
}