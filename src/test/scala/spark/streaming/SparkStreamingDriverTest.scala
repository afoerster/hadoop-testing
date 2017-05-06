package spark.streaming

import com.holdenkarau.spark.testing.StreamingSuiteBase
import org.junit
import org.scalatest.FunSuite
import org.testcontainers.containers.GenericContainer

class SparkStreamingDriverTest extends FunSuite with StreamingSuiteBase {
  val record = "VTS,2014-01-01 00:00:00,2014-01-01 00:01:00,1,0.32000000000000001,-73.995959999999997,40.758927,1,,-73.993229999999997,40.762841999999999,CSH,3.5,0.5,0.5,0,0,4.5"
  test("Transform record from string to WeatherEvent object") {
    val input = List(List(("key", record)))
    val expected = List(List(Events.fromJson(record)))
    testOperation[(String, String), Event](input, StreamHandler.transform _, expected, false)
  }

  // @TODO would need to read from FS to verify.
  ignore("Complex types dropped for saving to Parquet") {
    val ride: Event = Events.fromJson(record)
    val input = List(List(ride))
    val expected = List(List(ride))
    testOperation[Event, Event](input, StreamHandler.writeHDFS _, expected)
  }

  @junit.ClassRule
  val kafka = new GenericContainer("solr:5.3")

  kafka.withExposedPorts(8983)


}
