package spark.streaming

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
trait SolrRepository {
  def putEvent(event: Event) = {

  }
}