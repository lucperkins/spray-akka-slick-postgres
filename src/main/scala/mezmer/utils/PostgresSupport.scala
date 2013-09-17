package mezmer.utils

import slick.driver.PostgresDriver.simple._
import com.typesafe.config.ConfigFactory
import scala.util.Try

trait PostgresSupport {
  val pgConfig = ConfigFactory.load()
  lazy val (pgHost, pgPort, pgDB) = (
    Try(pgConfig.getString("postgres.host")).getOrElse("localhost"),
    Try(pgConfig.getInt("postgres.port")).getOrElse(5432),
    Try(pgConfig.getString("postgres.dbname")).getOrElse("localhost")
  )

  def db = Database.forURL(
    url    = "jdbc:postgresql://%s:%d/%s".format(pgHost, pgPort, pgDB),
    driver = "org.postgresql.Driver"
  )

  implicit val session: Session = db.createSession()
}

object PostgresSupport extends PostgresSupport