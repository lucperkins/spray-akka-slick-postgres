package mezmer.utils

import com.typesafe.config.ConfigFactory
import slick.driver.PostgresDriver.simple._

trait PostgresSupport {
  val pgConfig = ConfigFactory.load()
  lazy val (pgHost, pgPort, pgDBName, pgDriver) = (
    pgConfig.getString("postgres.host"),
    pgConfig.getInt("postgres.port"),
    pgConfig.getString("postgres.dbname"),
    pgConfig.getString("postgres.driver")
  )

  def db = Database.forURL(
    url    = s"jdbc:postgresql://$pgHost:$pgPort/$pgDBName",
    driver = pgDriver
  )

  implicit val session: Session = db.createSession()
}

object PostgresSupport extends PostgresSupport