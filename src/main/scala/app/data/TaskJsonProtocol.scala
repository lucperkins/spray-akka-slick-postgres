package app.data

import spray.json._
import DefaultJsonProtocol._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

import app.models.Task

object TaskJsonProtocol extends DefaultJsonProtocol {
  def dateTimeParse(dt: String): DateTime = ISODateTimeFormat.dateTimeParser().parseDateTime(dt)

  implicit object TaskJsonFormat extends RootJsonFormat[Task] {
    def write(t: Task) = JsObject(
      "taskId"   -> JsNumber(t.taskId.getOrElse(0)),
      "content"  -> JsString(t.content.toString()),
      "created"  -> JsString(t.created.toString()),
      "finished" -> JsBoolean(t.finished),
      "assignee" -> JsString(t.assignee.toString)
    )
    
    def read(j: JsValue) = {
      j.asJsObject.getFields("taskId", "content", "created", "finished", "assignee") match {
        case Seq(JsNumber(taskId), JsString(content), JsString(created), JsBoolean(finished), JsString(assignee)) =>
          Task(Some(taskId.toInt), content, dateTimeParse(created), finished, assignee)
        case _ => throw new DeserializationException("Improperly formed Task object")
      }
    }
  }
}