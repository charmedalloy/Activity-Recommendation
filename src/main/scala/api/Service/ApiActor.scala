package api.Service

import akka.actor.{Actor, ActorContext, ActorLogging, ActorRef, ActorRefFactory, Props}
import akka.util.Timeout
import api.Location
import org.json4s.{DefaultFormats, Formats}
import spray.http.HttpHeaders.RawHeader
import spray.http.MediaTypes
import spray.httpx.Json4sSupport
import spray.routing.{HttpService, Route}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
  * Created by avalj on 12/17/16.
  */

object Json4sProtocol extends Json4sSupport {
  implicit def json4sFormats: Formats = DefaultFormats
}

class ApiActor extends Actor with HttpService with ActorLogging {

  import Json4sProtocol._

  //timeout needs to be set as an implicit val for the ask method (?)
  implicit val timeout = Timeout(5.minutes)

  //This actor only runs our route, but you could add
  //other things here, like request stream processing or timeout handling
  def actorRefFactory: ActorContext = context
  def receive: Receive = runRoute(apiRoute)

  val apiRoute: Route =
    path("location" / Segment / Segment) { (lat, long) =>
      get {
        respondWithMediaType(MediaTypes.`application/json`) {
          respondWithHeader(RawHeader("Access-Control-Allow-Origin", "*")) {
            complete {
              Location.app(lat, long)
            }
          }
        }
      }
    }
}
