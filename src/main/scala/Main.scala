import cats.effect._
import org.scanamo.{ScanamoCats, Table}
import org.scanamo.syntax._
import org.scanamo.generic.auto._

object Main extends IOApp {

  case class Music(Artist: String, SongTitle: String, AlbumTitle: String)
  private val table = Table[Music]("Music")

  def run(args: List[String]): IO[ExitCode] = {
    val resources = new DynamoClientProvider[IO]
      .clientResource
      .map(client => ScanamoCats[IO](client))

    resources.use(client => {
      program(client)
    }).as(ExitCode.Success)
  }

  private def program(client: ScanamoCats[IO]): IO[Unit] = {
    for {
      r <- client.exec {
        table.get("Artist" === "No One You Know" and "SongTitle" === "Call Me Today")
      }
      _ <- printLn(r)
    } yield ()
  }

  def printLn(s: Any): IO[Unit] = IO {
    println(s)
  }
}
