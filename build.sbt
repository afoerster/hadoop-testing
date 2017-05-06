name := "hadoop-testing"

version := "1.0"

scalaVersion := "2.10.6"

val sparkV = "1.6.0-cdh5.8.2"
val hadoopV = "2.6.0-cdh5.8.2"

parallelExecution in Test := false

resolvers += "cloudera" at "https://repository.cloudera.com/artifactory/cloudera-repos/"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkV,
  "org.apache.spark" %% "spark-streaming" % sparkV % "provided",
  "org.apache.spark" %% "spark-streaming-kafka" % sparkV % "provided",
  "org.apache.spark" %% "spark-sql" % sparkV % "provided",
  "io.spray" %% "spray-json" % "1.3.2",
  "org.apache.solr" % "solr-solrj" % "4.10.3-cdh5.8.2",
  "org.apache.solr" % "solr-core" % "4.10.3-cdh5.8.2",
  "org.apache.hadoop" % "hadoop-hdfs" % hadoopV,
  "org.apache.hadoop" % "hadoop-hdfs" % hadoopV % "test" classifier "tests",
  "org.apache.hadoop" % "hadoop-common" % hadoopV,
  "org.apache.hadoop" % "hadoop-common" % hadoopV % "test" classifier "tests"

)

libraryDependencies ++= Seq(
  "com.holdenkarau" %% "spark-testing-base" % "1.6.1_0.3.3" % "test",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.testcontainers" % "testcontainers" % "1.1.6" % "test"
)