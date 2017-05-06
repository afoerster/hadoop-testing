package spark.streaming

import org.apache.spark.sql.{SQLContext, SaveMode}
import org.apache.spark.streaming.dstream.DStream

object StreamHandler extends SolrRepository with Serializable {

  def handle(stream: DStream[(String, String)]): Unit = {
    (transform _ andThen writeHDFS _ andThen writeSolr _) (stream)
  }

  def transform(stream: DStream[(String, String)]): DStream[Event] = {
    stream.map { case (key, value) =>
      Events.fromJson(value)
    }.filter(_.location == "west-1")
  }

  def writeSolr(stream: DStream[Event]): DStream[Event] = {

    val responses = stream.foreachRDD { rdd =>
      rdd.foreach(event => putEvent(event))
    }

    stream
  }

  def writeHDFS(stream: DStream[Event]): DStream[Event] = {
    stream.foreachRDD { events =>
      val sqlContext = SQLContext.getOrCreate(events.sparkContext)
      import sqlContext.implicits._

      val eventFrame = events
        .coalesce(1) // reduce file size. @TODO match to executor instances? ... but not possible to get num at runtime.
        .toDF

      // Drop complex fields. Impala can't do inserts on them during compactionx.
      val parquetEvents =
      eventFrame.drop($"pickup_location").drop($"dropoff_location")

      parquetEvents.write
        .partitionBy("dropoff_date")
        .mode(SaveMode.Append)
        .parquet("/data/rides/stream")
    }

    stream
  }
}
