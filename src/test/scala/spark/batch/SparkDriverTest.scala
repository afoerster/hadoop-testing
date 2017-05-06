package spark.batch

import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfterAll, FunSuite}

class SparkDriverTest extends FunSuite with BeforeAndAfterAll {

  val conf = new SparkConf().setAppName("local test").setMaster("local[*]")

  var sc: SparkContext = new SparkContext(conf)

  override def afterAll = {
    sc.stop
  }

  // The entire pipeline can be tested
  test("test entire workflow") {
    val data = sc.parallelize(Seq("1", "2", "3"))
    val result = SparkDriver.pipeline(data)

    assert(result.collect === Array(1, 4, 9))
  }

  // Conversion logic can be tested by itself
  test("Values are converted to Int") {
    val data = sc.parallelize(Seq("1", "2", "3"))
    val result = SparkDriver.convertToInt(data)

    assert(result.collect === Array(1, 2, 3))
  }

  // Squaring logic can be tested by itself
  test("Values are squared") {
    val data = sc.parallelize(Seq(1, 2, 3))
    val result = SparkDriver.square(data)

    assert(result.collect === Array(1, 4, 9))
  }
}
