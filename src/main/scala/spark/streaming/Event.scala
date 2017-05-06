package spark.streaming

import spray.json._
import DefaultJsonProtocol._

object EventJson extends DefaultJsonProtocol {
  implicit val eventFormat = jsonFormat8(Event)
}

/**
  *
  * Version (long) - The Osso event format version.
  * Event type ID (int) - A numeric event type.
  * Event ID (string) - An ID that uniquely identifies an event.
  * Timestamp (long) - A millisecond-accurate epoch timestamp indicating the moment the event occurred.
  * Location (string) - The location that generated the event.
  * Host (string) - The host or device that generated the event.
  * Service (string) - The service that generate the event.
  * Body (byte array) - The body or “payload” of the event.
  *
  */
case class Event(version: Long,
                 eventTypeId: Int,
                 eventId: String,
                 timestamp: Long,
                 location: String,
                 host: String,
                 service: String,
                 body: Seq[Char]) {

}

object Events {

  import EventJson._
  import spray.json._

  def fromJson(json: String): Event = {
      json.parseJson.convertTo[Event]
    }

  def toJson(event: Event): String = event.toJson.toString()
}

