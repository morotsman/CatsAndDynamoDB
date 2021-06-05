import cats.effect._
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.{DynamoDbAsyncClient}

import java.net.URI


class DynamoClientProvider[F[_] : Async](host: String, region: Region) {


  def clientResource: Resource[F, DynamoDbAsyncClient] =
    Resource.make(acquire)(release)

  private def acquire: F[DynamoDbAsyncClient] = Async[F].delay {
    DynamoDbAsyncClient.builder()
      .region(region)
      .endpointOverride(URI.create(s"http://$host:8000/"))
      .build()
  }

  private def release(client: DynamoDbAsyncClient): F[Unit] =
    Async[F].delay(client.close())

}
