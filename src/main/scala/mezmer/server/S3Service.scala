package mezmer.server

import java.io.{ ByteArrayInputStream, InputStream, OutputStream }
import spray.http._
import spray.http.{ BodyPart, HttpEntity, MultipartFormData }
import spray.http.MediaTypes._
import spray.httpx.marshalling.Marshaller
import spray.httpx.unmarshalling.Unmarshaller
import spray.json._
import DefaultJsonProtocol._
import spray.routing._
import spray.util._

import mezmer.utils.S3Support

trait S3Service extends WebService
	with S3Support
{
  case class S3Result(s3_upload_succeeded: Boolean)
  implicit val s3ResultFormat = jsonFormat1(S3Result)

  private def saveToS3(fileName: String, content: InputStream): Boolean = {
    try {
      val newFile = new java.io.File(fileName)
      val fos = new java.io.FileOutputStream(newFile)
      val buffer = new Array[Byte](999999999)
      Iterator
        .continually (content.read(buffer))
        .takeWhile (-1 !=)
        .foreach(read=>fos.write(buffer,0,read))
      uploadFile(fileName, newFile)
      true
    } catch {
      case _: Throwable => false
    }
  }

  val s3Routes = {
    path("file") {
      post {
        entity(as[MultipartFormData]) { formData =>
          complete {
            val res = formData.fields.map {
              case (name, BodyPart(entity, headers)) =>
                val content = new ByteArrayInputStream(entity.buffer)
                val contentType = headers.find(h => h.is("content-type")).get.value
                val fileName = headers.find(h => h.is("content-disposition")).get.value.split("filename=").last
                val result = saveToS3(fileName, content)
                result
              case _ =>
            }
            s"""{ result: $res }"""
          }
        }
      }
    }
  }
}