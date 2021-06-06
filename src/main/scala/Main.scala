import cats.effect._
import org.scanamo.{ScanamoCats, Table}

import software.amazon.awssdk.regions.Region

object Main extends IOApp {

  private val host = sys.env("HOST")
  private val region = sys.env.get("REGION").map(Region.of).get

  def run(args: List[String]): IO[ExitCode] = {
    val resources = new DynamoClientProvider(host, region)
      .clientResource
      .map(client => ScanamoCats[IO](client))

    resources.use(client => {
      DynamoDBProgram.program(client)
    }).as(ExitCode.Success)
  }
}
