import org.scalatest.funsuite.AnyFunSuite
import cats.effect.IO
import helpers.{DynamoDBServer, DynamoTable}
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType._
import org.scanamo.generic.auto._

class DynamoDBProgramTest extends AnyFunSuite with DynamoDBServer with DynamoTable {

  test("the content of the db should be the same after the program has run") {
    withTable[Music]("Music")("Artist" -> S){ (table, scanamo) =>
      val program: IO[Unit] = DynamoDBProgram.program(table, scanamo)

      val music = Set(
        Music("Artist1", "SongTitle1", "AlbumTitle"),
      )

      val expected = for {
        _ <- scanamo.exec {
          table.putAll(music)
        }
        before <- scanamo.exec {
          table.scan()
        }
        _ <- program
        after <- scanamo.exec {
          table.scan()
        }
      } yield {
        before.size == music.size && before == after
      }

      assert(expected.unsafeRunSync())
    }
  }

}