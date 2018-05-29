package app.models

import slick.driver.PostgresDriver.api._
import com.github.tototoshi.slick.PostgresJodaSupport._
import org.joda.time.DateTime
import spray.json._
import DefaultJsonProtocol._
import com.github.tototoshi.csv._
import app.utils.PostgresSupport

import scala.concurrent.{ExecutionContext, Future}

case class Task(
  taskId:   Option[Int] = None,
  content:  String,
  created:  DateTime,
  finished: Boolean,
  assignee: String
)

class TaskDAO(implicit val executionContext: ExecutionContext) extends PostgresSupport {
  import CSVConverter._
  import app.data.TaskJsonProtocol._

  class TaskTable(tag: Tag) extends Table[Task](tag, "tasks") {
    def taskId    = column[Int]     ("taskId", O.AutoInc, O.PrimaryKey, O.SqlType("BIGINT"))
    def content   = column[String]  ("content", O.SqlType("VARCHAR(50)"), O.NotNull)
    def created   = column[DateTime]("created", O.SqlType("TIMESTAMP"), O.NotNull)
    def finished  = column[Boolean] ("finished", O.SqlType("BOOLEAN"), O.NotNull)
    def assignee  = column[String]  ("assignee", O.SqlType("VARCHAR(20)"), O.NotNull)

    def *         = (taskId.?, content, created, finished, assignee) <> (Task.tupled, Task.unapply)
  }

  private val tasks = TableQuery[TaskTable]

  case class Count(numberOfTasks: Int)
  case class Ids(ids: List[Int])
  case class Result(result: String)
  implicit val countJsonFormat = jsonFormat1(Count)
  implicit val idsJsonFormat = jsonFormat1(Ids)
  implicit val resultFormat = jsonFormat1(Result)

  def pgResult(result: String): String = Result(result).toJson.compactPrint

  def numberOfTasks: Future[String] = db.run(tasks.length.result).map(r => Count(r).toJson.compactPrint)

  def listAllIds: Future[String] = db.run(tasks.map(_.taskId).result).map(ids => Ids(ids.toList).toJson.compactPrint)

  def listAllTasks: Future[String] = db.run(tasks.result).map(_.toJson.compactPrint)

  def createTable: Future[Unit] = db.run(tasks.schema.create)

  def dropTable: Future[Unit] = db.run(tasks.schema.drop)

  def addTask(content: String, assignee: String): Future[Int] =
    db.run(tasks += Task(content = content, created = new DateTime(), finished = false, assignee = assignee))

  def fetchTaskById(id: Int): Future[Option[Task]] = db.run(tasks.filter(_.taskId === id).result.headOption)

  def deleteTaskById(id: Int): Future[String] = db.run(tasks.filter(_.taskId === id).delete).collect {
    case 0 => pgResult("0 tasks deleted")
    case 1 => pgResult("1 task deleted")
    case n => pgResult(s"$n tasks deleted")
  }

  def updateTaskById(id: Int, newContent: String): Future[String] =
    db.run(tasks.filter(_.taskId === id).map(t => t.content).update(newContent)).collect {
      case 1 => pgResult(s"Task $id successfully modified")
      case _ => pgResult(s"Task $id was not found")
    }

  def addMultipleTasks(args: List[(String, String)]): Future[List[Int]] =
    Future.sequence(args.map(value => addTask(value._1, value._2)))

  def populateTable(filename: String): Future[List[Int]] = {
    val csvInfo = CSVConverter.convert(filename)
    addMultipleTasks(csvInfo)
  }

  def deleteAll: Future[String] =
    db.run(tasks.delete).collect {
      case 0 => pgResult("0 tasks deleted")
      case 1 => pgResult("1 task deleted")
      case n => pgResult(s"$n tasks deleted")
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