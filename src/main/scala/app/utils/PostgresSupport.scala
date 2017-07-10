package app.utils

import slick.driver.PostgresDriver.api._
import slick.jdbc.meta.MTable
import app.models.TaskDAO
import app.{Configs => C}

import scala.concurrent.{ExecutionContext, Future}

trait PostgresSupport {
  def db = Database.forURL(
    url    = s"jdbc:postgresql://${C.pgHost}:${C.pgPort}/${C.pgDBName}",
    driver = C.pgDriver
  )

  implicit val session: Session = db.createSession()

  def startPostgres(taskDAO: TaskDAO)(implicit executionContext: ExecutionContext): Future[Unit] =
    db.run(MTable.getTables("tasks")).map(v => if(v.isEmpty) taskDAO.createTable)

}