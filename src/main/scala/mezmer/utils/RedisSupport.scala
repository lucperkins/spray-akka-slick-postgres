package mezmer.utils

import com.redis.RedisClient
import com.typesafe.config.ConfigFactory

trait RedisSupport {
  val redisConfig = ConfigFactory.load()
  val (redisHost, redisPort) = (
    redisConfig.getString("redis.host"),
    redisConfig.getInt("redis.port")
  )

  val maybeRedisClient = new RedisClient(redisHost, redisPort)

  def client(c: RedisClient): RedisClient = c.connected match {
  	case true  => c
  	case false => new RedisClient(redisHost, redisPort)
  }
}