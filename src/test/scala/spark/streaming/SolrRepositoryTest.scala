package spark.streaming

import org.apache.solr.client.solrj.impl.HttpSolrServer
import org.apache.solr.common.SolrInputDocument
import org.junit
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.testcontainers.containers.GenericContainer

class SolrRepositoryTest extends FunSuite with BeforeAndAfterAll {

  @junit.ClassRule
  val solr = new GenericContainer("solr:5.3")
  solr.addFileSystemBind()

  solr.withExposedPorts(8983)

  override def afterAll(): Unit = {
    println("stopping")
    solr.stop()
  }

  override def beforeAll(): Unit = {
    println("starting")
    solr.start()
  }

  test("talk to solr") {
    val solrUrl =
      s"http://${solr.getContainerIpAddress}:${solr.getMappedPort(8983)}/solr"

    val server = new HttpSolrServer(solrUrl)

    (1 to 1000).map { n =>
      try {
        val doc = new SolrInputDocument()
        doc.addField("cat", "book")
        doc.addField("id", "book-" + n)
        doc.addField("name", "The Legend of the Hobbit part " + n)
        server.add(doc)

        if (n % 100 == 0) server.commit()
      } catch {
        case e: Throwable => println("failed" + e); Thread.sleep(10000)
      }
    }
    server.commit()

    println(solrUrl)
  }

}
