import cats.effect._
import org.scanamo.{ScanamoCats, Table}
import software.amazon.awssdk.regions.Region

import scala.util.Try

object Main extends IOApp {

  private val host = Try(sys.env("HOST"))
    .getOrElse(throw new RuntimeException("Please add mandatory env param host"))
  private val region = Try(sys.env.get("REGION").map(Region.of).get)
    .getOrElse(throw new RuntimeException("Please add mandatory env param region"))

  def run(args: List[String]): IO[ExitCode] = {
    val resources = new DynamoClientProvider(host, region)
      .clientResource
      .map(client => ScanamoCats[IO](client))

    resources.use(client => {
      DynamoDBProgram.program(client)
    }).as(ExitCode.Success)
  }
}
