package helpers

import cats.effect.IO
import org.scanamo.{DynamoFormat, LocalDynamoDB, ScanamoCats, Table}
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType

trait DynamoTable {
  private val client = LocalDynamoDB.client(8001)
  private val scanamo: ScanamoCats[IO] = ScanamoCats[IO](client)

  protected def withTable[A: DynamoFormat](tableName: String)(attributes: (String, ScalarAttributeType)*)(f: (Table[A], ScanamoCats[IO]) => Unit): Unit = {
    LocalDynamoDB.createTable(client)(tableName)(attributes: _*)
    val table = Table[A](tableName)
    f(table, scanamo)
    LocalDynamoDB.deleteTable(client)(tableName)
  }
}
