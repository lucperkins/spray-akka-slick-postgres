package mezmer.actors

import akka.actor._
// import mezmer.models.TweetDAO

object WebWorker {
  case object Go
}

class WebWorker extends Actor {
  import WebWorker.Go

  def receive: Receive = {
    case Go => s"It worked!"
  }
}