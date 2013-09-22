package mezmer.server

import spray.http._
import HttpCharsets._
import MediaTypes._
import spray.http.StatusCode
import spray.httpx.unmarshalling.Unmarshaller
import spray.json._

import mezmer.models.TaskDAO
import mezmer.server.HTTPHelpers._

trait TaskService extends WebService {

  /*case class Person(name: String, age: Int)
  val `application/person` = MediaTypes.register(MediaType.custom("application/person"))
  object Person {
    implicit val PersonUnmarshaller = Unmarshaller[Person](`application/person`) {
      case HttpEntity.NonEmpty(contentType, buffer) =>
        val Array(_, name, age) = buffer.asString.split(":,".toCharArray).map(_.trim)
      Person(name, age.toInt)
    }
  }*/

  val taskServiceRoutes = {
    pathPrefix("tasks") {
      path("") {
        get { ctx =>
          val allTasks: String = TaskDAO.listAllTasks
          ctx.complete(Ok(`application/json`, `UTF-8`, allTasks))
        } ~
          post {
            formFields('content, 'assignee) { (content, assignee) =>
              val pgResponse = TaskDAO.addTask(content, assignee)
              complete(Ok(`text/plain`, `UTF-8`, pgResponse))
            }
          }
      } ~
      path("count") {
        get { ctx =>
          val numberOfTasks: String = TaskDAO.numberOfTasks
          ctx.complete(Ok(`application/json`, `UTF-8`, numberOfTasks))
        }
      } ~
      path("ids") {
        get { ctx =>
          val ids: String = TaskDAO.listAllIds
          ctx.complete(Ok(`application/json`, `UTF-8`, ids))
        }
      } ~
      path("all") {
        delete { ctx =>
          val res: String = TaskDAO.deleteAll
          ctx.complete(Ok(`text/plain`, `UTF-8`, res))
        }
      }
    } ~
    pathPrefix("task" / IntNumber) { taskId =>
      get { ctx =>
        val task: String = TaskDAO.fetchTaskById(taskId)
        ctx.complete(Ok(`application/json`, `UTF-8`, task))
      } ~
        delete { ctx =>
          val pgResponse = TaskDAO.deleteTaskById(taskId)
          ctx.complete(Ok(`text/plain`, `UTF-8`, pgResponse))
        } ~
          put {
            formFields('content) { content =>
              val pgResponse = TaskDAO.updateTaskById(taskId, content)
              complete(Ok(`text/plain`, `UTF-8`, pgResponse))
            }
          }
    } ~
    path("api") {
      get {
        complete("\n  HTTP methods:\n  =============\n\n  GET /count\n  GET /tasks\n  GET /task/:id\n  PUT /task/:id\n  DELETE /task/:id\n  POST /tasks\n\n\n")
      }
    } ~
    path("populate" / Segment) { filename =>
      get {
        TaskDAO.populateTable(filename)
        complete("Table populated")
      }
    }
  }
}