package api.Service

import akka.actor.{Actor, ActorContext, ActorLogging, ActorRefFactory}
import api.MyApp
import org.json4s.{DefaultFormats, Formats}
import spray.http.HttpHeaders.RawHeader
import spray.http.MediaTypes
import spray.httpx.Json4sSupport
import spray.routing.{HttpService, Route}

/**
  * Created by avalj on 12/17/16.
  */

object Json4sProtocol extends Json4sSupport {
  implicit def json4sFormats: Formats = DefaultFormats
}

class ApiActor extends Actor with HttpService with ActorLogging {

  import Json4sProtocol._

  //The HttpService trait defines only one abstract member, which
  //connects the services environment to the enclosing actor or test
  def actorRefFactory: ActorContext = context

  //This actor only runs our route, but you could add
  //other things here, like request stream processing or timeout handling
  def receive: Receive = runRoute(apiRoute)

  val apiRoute: Route =
    path("location" / Segment / Segment) { (lat, long) =>
      get {
        respondWithMediaType(MediaTypes.`application/json`) {
          respondWithHeader(RawHeader("Access-Control-Allow-Origin", "*")) {
            complete {
              MyApp.app(lat, long)
            }
          }
        }
      }
    }
}
