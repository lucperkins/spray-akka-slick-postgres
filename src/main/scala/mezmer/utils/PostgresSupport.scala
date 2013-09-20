package mezmer.utils

import com.typesafe.config.ConfigFactory
import scala.util.Try
import slick.driver.PostgresDriver.simple._

trait PostgresSupport {
  val pgConfig = ConfigFactory.load()
  lazy val (pgHost, pgPort, pgDBName, pgDriver) = (
    Try(pgConfig.getString("postgres.host")).getOrElse("localhost"),
    Try(pgConfig.getInt("postgres.port")).getOrElse(5432),
    Try(pgConfig.getString("postgres.dbname")).getOrElse("postgres"),
    Try(pgConfig.getString("postgres.driver"))
  )

  def db = Database.forURL(
    url    = s"jdbc:postgresql://$pgHost:$pgPort/$pgDBName",
    driver = pgDriver
  )

  implicit val session: Session = db.createSession()
}

object PostgresSupport extends PostgresSupport