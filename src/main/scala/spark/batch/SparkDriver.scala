package spark.batch

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Main extends App {
  val conf = new SparkConf().setAppName("spark-submit app")
  val sc = new SparkContext(conf)

  val rdd = sc.textFile("some file")
  SparkDriver.pipeline(rdd)
}

object SparkDriver {
  def pipeline(rdd: RDD[String]): RDD[Int] = {
    (convertToInt _ andThen square _)(rdd)
  }

  def convertToInt(rdd: RDD[String]): RDD[Int] = {
    rdd.map(_.toInt)
  }

  def square(rdd: RDD[Int]): RDD[Int] = {
    rdd.map(x => x * x)
  }
}
