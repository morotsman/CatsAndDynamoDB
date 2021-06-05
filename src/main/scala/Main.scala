import cats.effect._
import org.scanamo.{ScanamoCats, Table}
import org.scanamo.generic.auto._
import software.amazon.awssdk.regions.Region

object Main extends IOApp {

  private val host = sys.env.getOrElse("HOST", "localhost")
  private val region = Region.of(sys.env.getOrElse("REGION", "eu-west-1"))
  private val table = Table[Music]("Music")

  def run(args: List[String]): IO[ExitCode] = {
    val resources = new DynamoClientProvider(host, region)
      .clientResource
      .map(client => ScanamoCats[IO](client))

    resources.use(client => {
      DynamoDBProgram.program(table, client)
    }).as(ExitCode.Success)
  }
}
