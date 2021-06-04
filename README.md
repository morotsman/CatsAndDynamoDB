Start by: docker-compose up

To be able to run:

aws dynamodb list-tables --endpoint-url http://localhost:8000


You must: 

niklasleopold@Niklass-Air CatsAndDynamoDB % aws configure                                                
AWS Access Key ID [None]:1

AWS Secret Access Key [None]:2

Default region name [None]: us-west-1

Default output format [None]:

[Instruction about docker]

[Cli examples]

Remember to add to each cli command (if running locally):

--endpoint-url http://localhost:8000

[Instruction about docker]: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.DownloadingAndRunning.html
[Cli examples]: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Tools.CLI.html