import cats.effect._
import cats.implicits._
import org.scanamo.{ScanamoCats, Table}
import org.scanamo._
import org.scanamo.query.UniqueKey
import org.scanamo.generic.auto._
import org.scanamo.syntax._
import org.scanamo._
import org.scanamo.syntax._
import org.scanamo.generic.auto._
import org.scanamo.generic.semiauto._
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType._

object Main extends IOApp {

  private val host = sys.env.getOrElse("HOST", "localhost")
  private val region = Region.of(sys.env.getOrElse("REGION", "eu-west-1"))

  case class Music(Artist: String, SongTitle: String, AlbumTitle: String, NewField: String)
  private val table = Table[Music]("Music")

  def run(args: List[String]): IO[ExitCode] = {
    val resources = new DynamoClientProvider[IO](host, region)
      .clientResource
      .map(client => ScanamoCats[IO](client))

    resources.use(client => {
      program(client)
    }).as(ExitCode.Success)
  }

  private def program(client: ScanamoCats[IO]): IO[Unit] = {
    for {
      lem <- client.exec {
        table.scan
      }
      _ <- printLn("Before putAll: " + lem)
      _ <-
        client.exec {
          table.putAll(addLowercaseField(lem))
        }
      rs <- client.exec {
        table.scan
      }
      _ <- printLn("After putAll: " + rs)
    } yield ()
  }

  private def items(rs: List[Either[DynamoReadError, Music]]): Set[Music] = rs.toSet
    .filter(_.isRight)
    .map(_.toOption.get)


  private def addLowercaseField(rs: List[Either[DynamoReadError, Music]]): Set[Music] = items(rs)
    .map(m => m.copy(NewField = m.Artist.toLowerCase()))

  private def printLn(s: Any): IO[Unit] = IO {
    println(s)
  }
}
