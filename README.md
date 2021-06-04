## Start local DynamoDB

docker-compose up

## Configure AWS console

> aws configure                                                
AWS Access Key ID [None]:1
AWS Secret Access Key [None]:2
Default region name [None]: us-west-1
Default output format [None]:

## Test setup:

> aws dynamodb list-tables --endpoint-url http://localhost:8000

You should get an empty list

## When running locally:

Remember to add --endpoint-url http://localhost:8000 to all AWS console commands

## Create table (used in demo)

> aws dynamodb create-table \
--table-name Music \
--attribute-definitions \
AttributeName=Artist,AttributeType=S \
AttributeName=SongTitle,AttributeType=S \
--key-schema AttributeName=Artist,KeyType=HASH AttributeName=SongTitle,KeyType=RANGE \
--provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 \
--endpoint-url http://localhost:8000

## add item

>aws dynamodb put-item \
--table-name Music \
--item \
'{"Artist": {"S": "No One You Know"}, "SongTitle": {"S": "Call Me Today"}, "AlbumTitle": {"S": "Somewhat Famous"}}' \
--return-consumed-capacity TOTAL
--endpoint-url http://localhost:8000


## References
[Instruction about docker]
[Cli examples]



[Instruction about docker]: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.DownloadingAndRunning.html
[Cli examples]: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Tools.CLI.html