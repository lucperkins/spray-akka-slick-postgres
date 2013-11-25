package app.utils

import slick.driver.PostgresDriver.simple._
import scala.slick.jdbc.meta.MTable

import app.models.TaskDAO
import app.{ Configs => C }

trait PostgresSupport {
  def db = Database.forURL(
    url    = s"jdbc:postgresql://${C.pgHost}:${C.pgPort}/${C.pgDBName}",
    driver = C.pgDriver
  )

  implicit val session: Session = db.createSession()

  def startPostgres() = {
    if (MTable.getTables("tasks").list.isEmpty) {
      TaskDAO.createTable
    }
  }
}