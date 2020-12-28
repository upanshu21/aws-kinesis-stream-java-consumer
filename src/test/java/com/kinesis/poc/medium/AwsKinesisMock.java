package com.kinesis.poc.medium;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
//import com.amazonaws.services.kinesis.AmazonKinesis;
//import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
//import com.amazonaws.services.kinesis.model.PutRecordRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.context.TestConfiguration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;

import java.net.URI;
import java.nio.ByteBuffer;

import static org.awaitility.Awaitility.await;

@TestConfiguration
public class AwsKinesisMock {

    public DynamoDbAsyncClient getDynamoDbAsyncClient() {
        return DynamoDbAsyncClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(() -> AwsBasicCredentials.create("test","test"))
                .endpointOverride(URI.create("http://localhost:4566"))
                .build();
    }


    public CloudWatchAsyncClient getCloudWatchAsyncClient() {
        return CloudWatchAsyncClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(() -> AwsBasicCredentials.create("test","test"))
                .endpointOverride(URI.create("http://localhost:4566"))
                .build();
    }

    public byte[] getEventPayload() throws JSONException {

        JSONObject internalJson = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        internalJson.put("recordStatus", "successful");
        internalJson.put("messageId", "1234");

        jsonObject.put("attributes", internalJson);

        return jsonObject.toString().getBytes();
    }

    public KinesisAsyncClient getKinesisMock() {
        return KinesisAsyncClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(() -> AwsBasicCredentials.create("test","test"))
                .endpointOverride(URI.create("http://localhost:4566"))
                .build();
    }

    public static void publishDataToKinesis() {
//        BasicAWSCredentials awsCredentials = new BasicAWSCredentials("test", "test");
//        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration("http://localhost:4566","us-east-1");
//        AmazonKinesisClientBuilder clientBuilder = AmazonKinesisClientBuilder.standard();
//        clientBuilder.setCredentials(new AWSStaticCredentialsProvider(awsCredentials));
//        clientBuilder.withEndpointConfiguration(endpointConfiguration);
//
//        AmazonKinesis amazonKinesis = clientBuilder.build();
//        clientBuilder.build();
//        amazonKinesis.createStream("test2", 1);
//        await().until(() ->
//                amazonKinesis.describeStream("test2").getStreamDescription().getStreamStatus().equals("ACTIVE")
//                );
//
//        //amazonKinesis.createStream("test", 1);
//        PutRecordRequest putRecordRequest = new PutRecordRequest()
//                .withStreamName("test2")
//                .withPartitionKey("abc")
//                .withData(ByteBuffer.wrap("hello".getBytes()));
//
//        amazonKinesis.putRecord(putRecordRequest);

    }

    public static void main(String[] args) {
        publishDataToKinesis();
    }

}

