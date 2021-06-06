import cats.effect.IO
import org.scanamo.{DynamoReadError, ScanamoCats, Table, TypeCoercionError}
import org.scanamo.generic.auto._
import cats.implicits._

object DynamoDBProgram {

  private val table = Table[Music]("Music")

  def program(client: ScanamoCats[IO]): IO[Unit] = for {
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

  private def items(lem: List[Either[DynamoReadError, Music]]): Set[Music] =
    lem.traverse(identity).map(_.toSet) match {
      case Right(v) => v
      case Left(e) => throw new RuntimeException(e.toString)
    }

  private def printLn(s: Any): IO[Unit] = IO {
    println(s)
  }

}
