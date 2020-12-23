package com.kinesis.poc.medium;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.kinesis.AbstractAmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import org.json.JSONException;
import org.json.JSONObject;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

public class AwsKinesisMock extends AbstractAmazonKinesis {

    public static DynamoDbAsyncClient get() {
        return DynamoDbAsyncClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(() -> AwsBasicCredentials.create("test","test"))
                .endpointOverride(URI.create("http://localhost:4566"))
                .build();
    }

    private static CreateTableRequest getCreateTableRequest(){
        return new CreateTableRequest()
                .withAttributeDefinitions(new AttributeDefinition("Name", ScalarAttributeType.S))
                .withKeySchema(new KeySchemaElement("Name", KeyType.HASH))
                .withProvisionedThroughput(new ProvisionedThroughput(10L, 10L))
                .withTableName("MyTable");
    }

    public static CloudWatchAsyncClient getCloudWatchAsyncClient() {
        return CloudWatchAsyncClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(() -> AwsBasicCredentials.create("test","test"))
                .endpointOverride(URI.create("http://localhost:4566"))
                .build();
    }

    public static byte[] getEventPayload() throws JSONException, UnsupportedEncodingException {

        JSONObject internalJson = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        internalJson.put("recordStatus", "successful");
        internalJson.put("messageId", "1234");

        jsonObject.put("attributes", internalJson);

        return jsonObject.toString().getBytes();
    }

    public static void publishDataToKinesis() throws UnsupportedEncodingException, JSONException, ExecutionException, InterruptedException {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials("test", "test");
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration("http://localhost:4566","us-east-1");


//        KinesisAsyncClient kinesisAsyncClient = KinesisAsyncClient.builder()
//                .region(Region.US_EAST_1)
//                .credentialsProvider(() -> AwsBasicCredentials.create("test", "test"))
//                .endpointOverride(URI.create("http://localhost:4566"))
//                .build();


        AmazonKinesisClientBuilder clientBuilder = AmazonKinesisClientBuilder.standard();
        clientBuilder.setCredentials(new AWSStaticCredentialsProvider(awsCredentials));
        clientBuilder.withEndpointConfiguration(endpointConfiguration);

        AmazonKinesis amazonKinesis = clientBuilder.build();
        clientBuilder.build();
//        kinesisAsyncClient.createStream()
      //  amazonKinesis.createStream("test",1);

        PutRecordRequest putRecordRequest = new PutRecordRequest()
                .withStreamName("test")
                .withPartitionKey("abc")
                .withData(ByteBuffer.wrap("hello".getBytes()));

        amazonKinesis.putRecord(putRecordRequest);

    }

    public static void main(String[] args) throws UnsupportedEncodingException, JSONException, ExecutionException, InterruptedException {
     publishDataToKinesis();
    }

}

