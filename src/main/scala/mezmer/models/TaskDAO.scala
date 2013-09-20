package mezmer.models

import scala.slick.driver.PostgresDriver.simple._
import scala.slick.driver.PostgresDriver
import com.github.tototoshi.slick.JodaSupport._
import org.joda.time.DateTime
import spray.json._

import mezmer.utils.PostgresSupport

case class Task(
  taskId:   Int,
  content:  String,
  created:  DateTime,
  finished: Boolean,
  assignee: String
)

object TaskDAO extends PostgresSupport {
  import mezmer.data.TaskJsonProtocol._

  object TaskTable extends Table[Task]("tasks") {
    def taskId   = column[Int]     ("taskId", O.AutoInc, O.PrimaryKey, O.DBType("BIGINT"))
    def content  = column[String]  ("content", O.DBType("VARCHAR(50)"), O.NotNull)
    def created  = column[DateTime]("created", O.DBType("TIMESTAMP"), O.NotNull)
    def finished = column[Boolean] ("finished", O.DBType("BOOLEAN"), O.NotNull)
    def assignee = column[String]  ("assignee", O.DBType("VARCHAR(20)"), O.NotNull)

    def *            = (taskId ~ content ~ created ~ finished ~ assignee) <> (Task, Task.unapply _)

    def forInsert    = (content ~ created ~ finished ~ assignee) returning taskId
  }

  // Convert count to JSON
  case class Count(total: Int)
  implicit val countJsonFormat = jsonFormat1(Count)

  def listAllTasks: String =
    Query(TaskTable).list.toJson.prettyPrint

  def numberOfTasks: String = Query(TaskTable).list.length.toJson.prettyPrint

  def createTable =
    TaskTable.ddl.create

  def dropTable =
    TaskTable.ddl.drop

  def addTask(content: String, assignee: String): String = {
    val now = new DateTime()
    val finished = false
    TaskTable.forInsert.insert(content, now, finished, assignee) match {
      case 0 => "Something went wrong"
      case n => s"Task $n added successfully"
    }
  }

  def fetchTaskById(id: Int): String = 
    Query(TaskTable).where(_.taskId is id).list.toJson.prettyPrint

  def deleteTaskById(id: Int): String = {
    TaskTable.filter(_.taskId === id).delete match {
      case 0 => s"Task $id was not found"
      case 1 => s"Task $id successfully deleted"
      case _ => "Something went wrong"
    }
  }

  def updateTaskById(id: Int, newContent: String): String = {
    TaskTable.where(_.taskId is id).
      map(t => t.content).
      update(newContent) match {
        case 1 => s"Task $id successfully modified"
        case _ => s"Task $id was not found"
      }
  }
}