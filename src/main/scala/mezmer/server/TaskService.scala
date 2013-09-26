package mezmer.server

import akka.actor._
import akka.io.IO
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global
import spray.http._
import HttpCharsets._
import MediaTypes._
import spray.json._
import spray.routing.Route

import mezmer.actors.PostgresActor
import mezmer.server.HTTPHelpers._

trait TaskService extends WebService {
  import PostgresActor._

  val postgresWorker = actorRefFactory.actorOf(Props[PostgresActor], "postgres-worker")
  
  def postgresCall(message: Any) =
    (postgresWorker ? message).mapTo[String].map(result => result)

  val taskServiceRoutes = {
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