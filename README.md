# Kinesis Data Stream Java Consumer

This a sample application which uses Kinesis Client Library(KCL 2.0x) to consume data from Kinesis Data Stream. 
The KCL automatically subscribes your application to all the shards of a stream, and ensures that your consumer application can read with a throughput value of 2 MB/sec per shard.
Consumer logic and application is seperate in this code so that you can directly implement your logic and not worry about the layer separations.

## Prerequsites

AWS account with access for: 
- Kinesis Data Stream
- DyanamoDB
- Cloudwatch
	
Provide the account credentials as environment variables before running the application.
If you are using Intellij as IDE you can refer to this [link](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/setup-credentials.html)

- Create a Data stream in your AWS account
- Name the data stream "test". You can name the stream according to your preference but remeber to change the stream name in consumer in the **KinesisScheduler** class.

## Running the application

Using **Intellij**:

- Run the **KinesisConsumerApplication** class which is the main application class
 
Using **maven**:

```
 - mvn spring-boot:run
```
 
 This will start the consumer application but there will be no output because we have not published any data on the stream.
 
### Publishing dummy data to the stream 

To Publish the Dummy data, do the following:

- Comment out the ```kinesisConfiguration.run();``` in the main class inside the run method.
- Add the below code inside the main class. 
	
```
	AmazonKinesisClientBuilder clientBuilder = AmazonKinesisClientBuilder.standard();
		AmazonKinesis amazonKinesis = clientBuilder.build();
		clientBuilder.build();
		//amazonKinesis.createStream("test", 1);  //uncomment to create a data stream via code with number of shard 1

		JSONObject internalJson = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		internalJson.put("recordStatus", "successful");
		internalJson.put("messageId", "1234");

		jsonObject.put("attributes", internalJson);
		byte[] sampleData = jsonObject.toString().getBytes();

		PutRecordRequest putRecordRequest = new PutRecordRequest()
				.withStreamName("test")
				.withPartitionKey("abc")
				.withData(ByteBuffer.wrap(sampleData));

		System.out.println(amazonKinesis.putRecord(putRecordRequest).getSequenceNumber());
```

This will publish the data to the stream **test**, replace **test** with your stream name.
The code snippet after publishing will also print the sequence number to confirm.
Remove the snippet after publishing the data and uncomment the ```kinesisConfiguration.run();``` inside the **run()** method and rerun the consumer.





