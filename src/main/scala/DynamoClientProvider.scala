import cats.effect._
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.{DynamoDbAsyncClient}

import java.net.URI


class DynamoClientProvider(host: String, region: Region) {

  def clientResource: Resource[IO, DynamoDbAsyncClient] =
    Resource.make(acquire)(release)

  private def acquire: IO[DynamoDbAsyncClient] = Async[IO].delay {
    DynamoDbAsyncClient.builder()
      .region(region)
      .endpointOverride(URI.create(s"http://$host:8000/"))
      .build()
  }

  private def release(client: DynamoDbAsyncClient): IO[Unit] =
    Async[IO].delay(client.close())

}
