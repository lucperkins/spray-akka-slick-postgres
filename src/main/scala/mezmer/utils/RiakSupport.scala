package mezmer.utils

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import nl.gideondk.raiku._
import spray.json._
import DefaultJsonProtocol._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{ Try, Success, Failure }

object RiakSupport {
  val timeout = Duration(100, SECONDS)

  case class Person(id: Int, name: String)

  val riakConfig = ConfigFactory.load()
  val (riakHost, riakPort, riakNodes) = (
    riakConfig.getString("riak.host"),
    riakConfig.getInt("riak.port"),
    riakConfig.getInt("riak.nodes")
  )
  implicit val system = ActorSystem("system")
  val raikuClient = RaikuClient(riakHost, riakPort, riakNodes)
  implicit val personFormat = jsonFormat2(Person)
  implicit val personConverter = RaikuConverter.newConverter(
    reader = (v: RaikuRWValue) => personFormat.read(new String(v.data).asJson),
    writer = (p: Person) => RaikuRWValue(p.id.toString, p.toJson.toString.getBytes, "application/json"),
    binIndexes = (p: Person) => Map("id" -> Set(p.id.toString)),
    intIndexes = (p: Person) => Map("id" -> Set(p.id))
  )
  val bucket = RaikuBucket[Person]("people", raikuClient)

  case class Result(result: String)
  implicit val resultFormat = jsonFormat1(Result)

  def fetchPerson(id: Int) = {
    val get = bucket.fetch(id.toString).start.map {
      case Success(v) => v.flatMap(_.value)
      case Failure(e) => throw e
    }
    Await.result(get, timeout) match {
      case Some(x) => x.toJson.compactPrint
      case None    => new Result("none").toJson.compactPrint
    }
  }

  def storePerson(p: Person) = {
    val post = bucket.store(p).start.map {
      case Success(_) => new Result("success").toJson.compactPrint
      case Failure(_) => new Result("failure").toJson.compactPrint
    }
    Await.result(post, timeout)
  }

  def storeMultiple(ps: List[Person]) = {
    val post = (bucket <<* ps).start.map {
      case Success(_) => new Result("success").toJson.compactPrint
      case Failure(_) => new Result("failure").toJson.compactPrint
    }
    Await.result(post, timeout)
  }

  def deletePerson(p: Person) = {
    val delete = bucket.delete(p).start.map {
      case Success(_) => new Result("success").toJson.compactPrint
      case Failure(_) => new Result("failure").toJson.compactPrint
    }
    Await.result(delete, timeout)
  }
}