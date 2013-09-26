package mezmer.models

import scala.slick.driver.PostgresDriver
import scala.slick.driver.PostgresDriver.simple._
import com.github.tototoshi.slick.JodaSupport._
import org.joda.time.DateTime
import spray.json._
import com.github.tototoshi.csv._

import mezmer.utils.PostgresSupport

case class Task(
  taskId:   Int,
  content:  String,
  created:  DateTime,
  finished: Boolean,
  assignee: String
)

object TaskDAO extends PostgresSupport {
  import CSVConverter._
  import mezmer.data.TaskJsonProtocol._

  object TaskTable extends Table[Task]("tasks") {
    def taskId    = column[Int]     ("taskId", O.AutoInc, O.PrimaryKey, O.DBType("BIGINT"))
    def content   = column[String]  ("content", O.DBType("VARCHAR(50)"), O.NotNull)
    def created   = column[DateTime]("created", O.DBType("TIMESTAMP"), O.NotNull)
    def finished  = column[Boolean] ("finished", O.DBType("BOOLEAN"), O.NotNull)
    def assignee  = column[String]  ("assignee", O.DBType("VARCHAR(20)"), O.NotNull)

    def *         = (taskId ~ content ~ created ~ finished ~ assignee) <> (Task, Task.unapply _)

    def forInsert = (content ~ created ~ finished ~ assignee) returning taskId
  }

  def numberOfTasks: String = {
    case class Count(numberOfTasks: Int)
    implicit val countJsonFormat = jsonFormat1(Count)
    val count: Int = Query(TaskTable).list.length
    new Count(count).toJson.prettyPrint
  }

  def listAllIds: String = {
    case class Ids(ids: List[Int])
    implicit val idsJsonFormat = jsonFormat1(Ids)
    val ids = Query(TaskTable).map(_.taskId).list
    new Ids(ids).toJson.prettyPrint
  }

  def listAllTasks: String =
    Query(TaskTable).list.toJson.prettyPrint

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

  def addMultipleTasks(args: List[(String, String)]) = {
    args.map(arg => addTask(arg._1, arg._2)).map(result => println(result))
  }

  def populateTable(filename: String) = {
    val csvInfo = CSVConverter.convert(filename)
    addMultipleTasks(csvInfo)
  }

  def deleteAll = {
    Query(TaskTable).delete match {
      case 0 => "0 tasks deleted"
      case 1 => "1 task deleted"
      case n => s"$n tasks deleted"
    }
  }
}

object CSVConverter {
  import java.io.File
  import scala.collection.mutable.ListBuffer

  def convert(filename: String) = {
    val reader = CSVReader.open(new File(filename))
    val rawList = reader.iterator.toList
    val tweets = new ListBuffer[(String, String)]
    rawList.foreach(line => tweets ++= List((line(0), line(1))))
    tweets.toList
  }
}