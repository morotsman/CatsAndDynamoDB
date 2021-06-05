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
--key-schema AttributeName=Artist,KeyType=HASH \
--provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 \
--endpoint-url http://localhost:8000

## add item

> aws dynamodb put-item \
--table-name Music \
--item \
'{"Artist": {"S": "Artist1"}, "SongTitle": {"S": "Song1"}, "AlbumTitle": {"S": "Album1"}}' \
--return-consumed-capacity TOTAL \
--endpoint-url http://localhost:8000

## Delete table

> aws dynamodb delete-table \
--table-name Music \
--endpoint-url http://localhost:8000

## Docker

sbt assembly

docker build -t "niklastest" .

docker run -e AWS_ACCESS_KEY_ID=2 -e AWS_SECRET_ACCESS_KEY=2 -it "niklastest"

## Trace after first time docker-compose up

>app-node          | Start program
app-node          | Before putAll: List(Right(Music(Artist1,Song1,Album1)))
app-node          | After putAll: List(Right(Music(Artist1,Song1,Album1)), Right(Music(Artist1,Song1,Album1)))
app-node          | Program completed

## Trace after first second docker-compose up

>app-node          | Start program
app-node          | Before putAll: List(Right(Music(Artist1,Song1,Album1)), Right(Music(Artist1,Song1,Album1)))
dynamodb-local    | Jun 05, 2021 7:47:58 AM com.almworks.sqlite4java.Internal log
dynamodb-local    | WARNING: [sqlite] SQLiteDBAccess$14@4b0e6f64: job exception
dynamodb-local    | com.amazonaws.services.dynamodbv2.local.shared.exceptions.LocalDBAccessException: Given key conditions were not unique. Returned: [{Artist=AttributeValue: {S:Artist1}, SongTitle=AttributeValue: {S:Song1}, AlbumTitle=AttributeValue: {S:Album1}}] and [{Artist=AttributeValue: {S:Artist1}, AlbumTitle=AttributeValue: {S:Album1}, SongTitle=AttributeValue: {S:Song1}}].
dynamodb-local    | 	at com.amazonaws.services.dynamodbv2.local.shared.access.LocalDBUtils.ldAccessFail(LocalDBUtils.java:799)
dynamodb-local    | 	at com.amazonaws.services.dynamodbv2.local.shared.access.sqlite.SQLiteDBAccessJob.getRecordInternal(SQLiteDBAccessJob.java:224)
dynamodb-local    | 	at com.amazonaws.services.dynamodbv2.local.shared.access.sqlite.SQLiteDBAccess$14.doWork(SQLiteDBAccess.java:1555)
dynamodb-local    | 	at com.amazonaws.services.dynamodbv2.local.shared.access.sqlite.SQLiteDBAccess$14.doWork(SQLiteDBAccess.java:1551)
dynamodb-local    | 	at com.amazonaws.services.dynamodbv2.local.shared.access.sqlite.AmazonDynamoDBOfflineSQLiteJob.job(AmazonDynamoDBOfflineSQLiteJob.java:117)
dynamodb-local    | 	at com.almworks.sqlite4java.SQLiteJob.execute(SQLiteJob.java:372)
dynamodb-local    | 	at com.almworks.sqlite4java.SQLiteQueue.executeJob(SQLiteQueue.java:534)
dynamodb-local    | 	at com.almworks.sqlite4java.SQLiteQueue.queueFunction(SQLiteQueue.java:667)
dynamodb-local    | 	at com.almworks.sqlite4java.SQLiteQueue.runQueue(SQLiteQueue.java:623)
dynamodb-local    | 	at com.almworks.sqlite4java.SQLiteQueue.access$000(SQLiteQueue.java:77)
dynamodb-local    | 	at com.almworks.sqlite4java.SQLiteQueue$1.run(SQLiteQueue.java:205)
dynamodb-local    | 	at java.lang.Thread.run(Thread.java:748)
dynamodb-local    |
dynamodb-local    | Jun 05, 2021 7:47:58 AM com.almworks.sqlite4java.Internal log

## References

[Instruction about docker]

[Cli examples]



[Instruction about docker]: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.DownloadingAndRunning.html
[Cli examples]: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Tools.CLI.html