package api.Service

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import org.apache.log4j.{Level, Logger}
import spray.can.Http

import scala.concurrent.duration._

/**
  * Created by avalj on 12/17/16.
  */
object ApiApp extends App {
  Logger.getLogger("akka").setLevel(Level.ERROR)

  //we need an ActorSystem to host our application in
  implicit val system = ActorSystem("ApiApp")

  //create apiActor
  val apiActor = system.actorOf(Props[ApiActor], "ApiActor")

  //timeout needs to be set as an implicit val for the ask method (?)
  //implicit val timeout = Timeout(10.minutes)

  //start a new HTTP server on port 8080 with apiActor as the handler
  IO(Http) ! Http.Bind(apiActor, interface = "localhost", port = 8080)
}
