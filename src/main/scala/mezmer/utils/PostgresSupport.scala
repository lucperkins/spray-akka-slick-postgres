package mezmer.utils

import slick.driver.PostgresDriver.simple._
import com.typesafe.config.ConfigFactory

trait PostgresSupport {
  val pgConfig = ConfigFactory.load()
  val (pgHost, pgPort, pgDB) = (
    pgConfig.getString("postgres.host"),
    pgConfig.getInt("postgres.port"),
    pgConfig.getString("postgres.dbname.1")
  )

  def db = Database.forURL(
    url    = "jdbc:postgresql://%s:%d/%s".format(pgHost, pgPort, pgDB),
    driver = "org.postgresql.Driver"
  )

  implicit val session: Session = db.createSession()
}

object PostgresSupport extends PostgresSupport