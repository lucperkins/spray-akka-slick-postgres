package app.actors

import akka.actor.{Actor, Props}
import akka.pattern.pipe
import app.models.TaskDAO

object PostgresActor {
  case object FetchAll
  case class  CreateTask(content: String, assignee: String)
  case class  FetchTask(id: Int)
  case class  ModifyTask(id: Int, content: String)
  case class  DeleteTask(id: Int)
  case object DeleteAll
  case object GetCount
  case class  Populate(file: String)
  case object GetIds
  case object CreateTable
  case object DropTable

  def apply(taskDAO: TaskDAO) = Props(new PostgresActor(taskDAO))
}

final class PostgresActor(taskDAO: TaskDAO) extends Actor {
  import PostgresActor._
  import context.dispatcher

  def receive: Receive = {
    case FetchAll =>
      taskDAO.listAllTasks.pipeTo(sender)

    case CreateTask(content: String, assignee: String) =>
      taskDAO.addTask(content, assignee).pipeTo(sender)

    case FetchTask(id: Int) =>
      taskDAO.fetchTaskById(id).pipeTo(sender)

    case ModifyTask(id: Int, content: String) =>
      taskDAO.updateTaskById(id, content).pipeTo(sender)

    case DeleteTask(id: Int) =>
      taskDAO.deleteTaskById(id).pipeTo(sender)

    case DeleteAll =>
      taskDAO.deleteAll.pipeTo(sender)

    case GetCount =>
      taskDAO.numberOfTasks.pipeTo(sender)

    case Populate(file: String) =>
      taskDAO.populateTable(file).pipeTo(sender)

    case GetIds =>
      taskDAO.listAllIds.pipeTo(sender)

    case CreateTable =>
      taskDAO.createTable.pipeTo(sender)

    case DropTable =>
      taskDAO.dropTable.pipeTo(sender)
  }
}