package spark.streaming

package io.phdata.isk.consume

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object StreamConsumerMain extends App with StreamConsumer {
  start()
}

trait StreamConsumer {
  def start(): Unit = {
    val STREAM_INTERVAL = Seconds(4)

    val sparkConf = new SparkConf().setAppName("IoT Consumer")
    val sc = new SparkContext(sparkConf)
    val ssc = new StreamingContext(sc, STREAM_INTERVAL)
    val KAFKA_HOST = "kafka"
    val ZOOKEEPER_QUORUM = s"zookeeper:2181"
    val TOPIC_MAP = Map("events" -> 3)

    val stream: InputDStream[(String, String)] = KafkaUtils.createStream(
      ssc,
      ZOOKEEPER_QUORUM,
      "we_read_events",
      TOPIC_MAP
    )

    // workaround for: https://issues.apache.org/jira/browse/SPARK-7398
    // @TODO better workaround may be this: http://apache-spark-developers-list.1001551.n3.nabble.com/SparkStreaming-Workaround-for-BlockNotFound-Exceptions-td12096.html
    stream.persist(StorageLevel.MEMORY_AND_DISK)

    StreamHandler.handle(stream)

    ssc.start()
    ssc.awaitTermination()
  }
}
