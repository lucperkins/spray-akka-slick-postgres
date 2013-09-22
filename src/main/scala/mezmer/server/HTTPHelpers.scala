package mezmer.server

import spray.http._
import HttpCharsets._
import MediaTypes._

object HTTPHelpers {
  def Ok(ct: MediaType = `text/plain`, encoding: HttpCharset, s: String): HttpResponse =
    new HttpResponse(StatusCodes.OK, HttpEntity(contentType = ContentType(ct, encoding), string = s)) 
}