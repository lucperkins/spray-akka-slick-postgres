package mezmer.actors

import akka.actor.Actor

import mezmer.models.TaskDAO

object PostgresActor {
  case object FetchAll
  case class CreateTask(content: String, assignee: String)
  case class FetchTask(id: Int)
  case class ModifyTask(id: Int, content: String)
  case class DeleteTask(id: Int)
}

class PostgresActor extends Actor {
  import PostgresActor._

  def receive: Receive = {
    case FetchAll =>
      sender ! TaskDAO.listAllTasks
    case CreateTask(content: String, assignee: String) =>
      sender ! TaskDAO.addTask(content, assignee)
    case FetchTask(id: Int) =>
      sender ! TaskDAO.fetchTaskById(id)
    case ModifyTask(id: Int, content: String) =>
      sender ! TaskDAO.updateTaskById(id, content)
    case DeleteTask(id: Int) =>
      sender ! TaskDAO.deleteTaskById(id)
  }
}