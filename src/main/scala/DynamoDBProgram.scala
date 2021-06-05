import cats.effect.IO
import org.scanamo.{DynamoReadError, ScanamoCats, Table}

object DynamoDBProgram {

  def program(table: Table[Music], client: ScanamoCats[IO]): IO[Unit] = {
    for {
      _ <- printLn("Start program")
      lem <- client.exec {
        table.scan
      }
      _ <- printLn("Before putAll: " + lem)
      _ <-
        client.exec {
          table.putAll(items(lem))
        }
      rs <- client.exec {
        table.scan
      }
      _ <- printLn("After putAll: " + rs)
      _ <- printLn("Program completed")
    } yield ()
  }

  private def items(lem: List[Either[DynamoReadError, Music]]): Set[Music] = lem.toSet
    .filter(_.isRight)
    .map(_.toOption.get)

  private def printLn(s: Any): IO[Unit] = IO {
    println(s)
  }

}
