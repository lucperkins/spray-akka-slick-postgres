package mezmer.server

import spray.http._
import MediaTypes._
import HttpCharsets._
import spray.json._

import mezmer.models.TaskDAO

trait TaskService extends WebService {
  val taskServiceRoutes = {
    path("count") {
      get {
        val numberOfTasks: String = TaskDAO.numberOfTasks
        val res = HttpEntity(
          contentType = ContentType(`application/json`),
          string = numberOfTasks
        )
        complete(res)
      }
    } ~
    path("tasks") {
      get { ctx =>
        val allTasks: String = TaskDAO.listAllTasks
        val res = HttpEntity(
          contentType = ContentType(`application/json`, `UTF-8`),
          string = allTasks
        )
        ctx.complete(res)
      } ~
        post {
          formFields('content, 'assignee) { (content, assignee) =>
            val pgResponse = TaskDAO.addTask(content, assignee)
            val res = HttpEntity(
              contentType = ContentType(`text/plain`),
              string = pgResponse
            )
            complete(res)
          }
        }
    } ~
    pathPrefix("task" / IntNumber) { taskId =>
      get { ctx =>
        val task: String = TaskDAO.fetchTaskById(taskId)
        val res = HttpEntity(
          contentType = ContentType(`application/json`, `UTF-8`),
          string = task
        )
        ctx.complete(res)
      } ~
        delete { ctx =>
          val pgResponse = TaskDAO.deleteTaskById(taskId)
          val res = HttpEntity(
            contentType = ContentType(`text/plain`),
            string = pgResponse
          )
          ctx.complete(res)
        } ~
          put {
            formFields('content) { content =>
              val pgResponse = TaskDAO.updateTaskById(taskId, content)
              val res = HttpEntity(
                contentType = ContentType(`text/plain`),
                string = pgResponse
              )
              complete(res)
            }
          }
    } ~
    path("api") {
      get {
        complete("\n  HTTP methods:\n  =============\n\n  GET /count\n  GET /tasks\n  GET /task/:id\n  PUT /task/:id\n  DELETE /task/:id\n  POST /tasks\n\n\n")
      }
    }
  }
}