package spark.streaming

import com.google.common.base.Charsets
import org.scalatest.FunSuite

class EventTest extends FunSuite{
  test("Event converts to json") {

  }

  test("Event converts from json") {
    val event = Event(1, 1, "AXB", 112121212, "west", "host", "service", "message".toList)

    assert((Events.toJson _ andThen Events.fromJson _) (event) == event)
  }
}
