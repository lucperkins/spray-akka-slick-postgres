package app

import com.typesafe.config.ConfigFactory
import org.slf4j.{ Logger, LoggerFactory }

object Configs {
  val c = ConfigFactory.load()
  
  val interface    = c.getString("app.interface")
  val appPort      = c.getInt("app.port")
  val pgHost       = c.getString("postgres.host")
  val pgPort       = c.getInt("postgres.port")
  val pgDBName     = c.getString("postgres.dbname")
  val pgDriver     = c.getString("postgres.driver")

  val log: Logger = LoggerFactory.getLogger(this.getClass)
}