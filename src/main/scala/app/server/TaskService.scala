package app.server

import akka.pattern.ask

import scala.concurrent.ExecutionContext.Implicits.global
import spray.json._
import app.actors.PostgresActor
import app.models.TaskDAO
import spray.routing.Route

trait TaskService extends WebService {
  import PostgresActor._

  def taskServiceRoutes(taskDAO: TaskDAO): Route = {
    val postgresWorker = actorRefFactory.actorOf(PostgresActor.apply(taskDAO), "postgres-worker")

    def postgresCall(message: Any) =
    (postgresWorker ? message).mapTo[String].map(result => result)

    pathPrefix("tasks") {
      path("") {
        get { ctx =>
          ctx.complete(postgresCall(FetchAll))
        } ~
          post {
            formFields('content.as[String], 'assignee.as[String]) { (content, assignee) =>
              complete(postgresCall(CreateTask(content, assignee)))
            }
          }
      } ~
      path("count") {
        get { ctx =>
          ctx.complete(postgresCall(GetCount))
        }
      } ~
      path("all") {
        delete { ctx =>
          ctx.complete(postgresCall(DeleteAll))
        }
      } ~
      path("populate" / Segment) { filename =>
        post { ctx =>
          complete(postgresCall(Populate(filename)))
        }
      } ~
      path("ids") {
        get { ctx =>
          ctx.complete(postgresCall(GetIds))
        }
      } ~
      path("table") {
        get { ctx =>
          ctx.complete(postgresCall(CreateTable))
        } ~
          delete { ctx =>
            ctx.complete(postgresCall(DropTable))
          }
      }
    } ~
    path("task" / IntNumber) { taskId =>
      get { ctx =>
        ctx.complete(postgresCall(FetchTask(taskId)))
      } ~
        put {
          formFields('content.as[String]) { (content) =>
            complete(postgresCall(ModifyTask(taskId, content)))
          }
        } ~
          delete { ctx =>
            ctx.complete(postgresCall(DeleteTask(taskId)))
          }
    }
  }
}