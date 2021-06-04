import cats.effect._
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.{DynamoDbAsyncClient, DynamoDbAsyncClientBuilder}

import java.net.URI


class DynamoClientProvider[F[_] : Async] {

  def clientResource: Resource[F, DynamoDbAsyncClient] =
    Resource.make(acquire)(release)

  private def acquire: F[DynamoDbAsyncClient] = Async[F].delay {
    DynamoDbAsyncClient.builder()
      .region(Region.US_WEST_1)
      .endpointOverride(URI.create("http://localhost:8000/"))
      .build()
  }

  private def release(client: DynamoDbAsyncClient): F[Unit] =
    Async[F].delay(client.close())

}
