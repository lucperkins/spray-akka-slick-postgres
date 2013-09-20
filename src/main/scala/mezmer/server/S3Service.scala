package mezmer.server

import mezmer.utils.S3Support
import mezmer.server._
import java.io.File
import spray.http._
import MediaTypes._

trait S3Service extends WebService
  with S3Support
{
	val s3Routes = {
		(path("s3") & (post | put)) {
      complete(HttpEntity(contentType = ContentType(`text/plain`), string = "OK"))
    }
  }
}