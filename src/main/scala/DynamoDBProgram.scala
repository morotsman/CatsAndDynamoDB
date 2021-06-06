import cats.effect.IO
import org.scanamo.{DynamoReadError, ScanamoCats, Table}
import org.scanamo.generic.auto._

object DynamoDBProgram {

  private val table = Table[Music]("Music")

  def program(client: ScanamoCats[IO]): IO[Unit] = {
    for {
      _ <- printLn("Start program")
      lem1 <- client.exec {
        table.scan
      }
      _ <- printLn("Before first putAll: " + lem1)
      _ <- client.exec {
          table.putAll(items(lem1))
        }
      lem2 <- client.exec {
        table.scan
      }
      _ <- printLn("After first putAll: " + lem2)
      _ <- client.exec {
        table.putAll(items(lem2))
      }
      lem3 <- client.exec {
        table.scan
      }
      _ <- printLn("After second putAll: " + lem3)
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
