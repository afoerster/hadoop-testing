package hadoop

import java.io._

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.hdfs.{HdfsConfiguration, MiniDFSCluster}
import org.apache.hadoop.test.PathUtils
import org.scalatest.FunSuite

object LocalHdfs {
  val conf = new HdfsConfiguration()

  System.clearProperty(MiniDFSCluster.PROP_TEST_BUILD_DATA)

  val testDataPath = new File(PathUtils.getTestDir(getClass()), "miniclusters")

  val dataPath = new File(testDataPath, "cluster").getAbsolutePath()

  println(s"data dir: '$dataPath'")

  conf.set(MiniDFSCluster.HDFS_MINIDFS_BASEDIR, dataPath)

  val cluster = new MiniDFSCluster.Builder(conf).build()

}

class MiniClusters extends FunSuite {
  test("hdfs mini cluster") {

    val fs = FileSystem.get(LocalHdfs.conf)

    val path = new Path("/testfile")
    println("path: " + path.toUri)

    val os = fs.create(path)
    val writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"))
    writer.write("hello")

    writer.close()

    os.close()

    val contents = readFile(path, fs)

    println("contents: " + contents)

    val app = fs.append(path)
    val appender = new BufferedWriter(new OutputStreamWriter(app, "UTF-8"))
    appender.write(" world")
    appender.close()
    app.close()
    val appended = readFile(path, fs)
    println("appended: " + appended)
  }

  def readFile(path: Path, fs: FileSystem) = {

    val reader = new BufferedReader(new InputStreamReader(fs.open(path)))

    Stream.continually(reader.readLine).takeWhile(_ != null).mkString("\n")
  }
}
