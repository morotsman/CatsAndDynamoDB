package helpers

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer
import org.scalatest.{BeforeAndAfterAll, Suite}
import scala.util.Try

trait DynamoDBServer extends BeforeAndAfterAll {
  this: Suite =>
  // this is key, it's what allows the JVM to find our native lib dependencies
  System.setProperty("sqlite4java.library.path", "native-libs")

  private val server: Try[DynamoDBProxyServer] = Try {
    ServerRunner.createServerFromCommandLineArgs(Array("-inMemory"))
  }

  override def beforeAll(): Unit = {
    super.beforeAll()
    server.foreach(_.start())
  }

  override def afterAll(): Unit = {
    super.afterAll()
    server.foreach(_.stop())
  }
}
