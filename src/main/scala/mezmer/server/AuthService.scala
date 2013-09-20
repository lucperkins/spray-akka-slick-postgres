package mezmer.server

import spray.http._
import MediaTypes._
import spray.routing.authentication.BasicAuth

// type MezmerAuthenticator[User] = RequestContext => Future[Authentication[User]]
// type Authentication[User] = Either[Rejection, User]

trait AuthService extends WebService {
	val authRoutes = {
		/*path("login") {
			get {
				authorize(httpBasic(realm = "main")) { user =>
					complete(user.toString)
				}
			}
		}*/
	}
}