package mezmer.utils

import akka.actor.ActorSystem
import nl.gideondk.raiku.RaikuClient
import com.typesafe.config.ConfigFactory

trait RiakSupport {
  val riakConfig = ConfigFactory.load()
  val (riakHost, riakPort, riakNodes) = (
    riakConfig.getString("riak.host"),
    riakConfig.getInt("riak.port"),
    riakConfig.getInt("riak.nodes")
  )

  implicit val riakSystem = ActorSystem("riak-system")
  val raikuClient = RaikuClient(riakHost, riakPort, riakNodes)
}