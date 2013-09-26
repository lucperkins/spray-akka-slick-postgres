package mezmer.server

import spray.http._
import HttpCharsets._
import MediaTypes._

object HTTPHelpers {
  def Ok(ct: MediaType = `text/plain`, s: String): HttpResponse =
    new HttpResponse(StatusCodes.OK, HttpEntity(ContentType(ct, `UTF-8`), s))

  def OkJson(s: String): HttpResponse =
    new HttpResponse(StatusCodes.OK, HttpEntity(ContentType(`application/json`, `UTF-8`), s))
}