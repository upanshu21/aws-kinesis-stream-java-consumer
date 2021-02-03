package com.kinesis.poc;

import cloud.localstack.LocalstackTestRunner;
import cloud.localstack.awssdkv2.TestUtils;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RunWith(LocalstackTestRunner.class)
@SpringBootTest
public class KinesisConsumerMediumTests {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();
    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();


    @Test
    public void testGetRecord() throws Exception{
        String streamName = "test-s-"+ UUID.randomUUID().toString();
        KinesisAsyncClient kinesisClient = TestUtils.getClientKinesisAsyncV2();

        CreateStreamRequest request = CreateStreamRequest.builder()
                .streamName(streamName).shardCount(1).build();
        CreateStreamResponse response = kinesisClient.createStream(request).get();
        Assert.assertNotNull(response);
        TimeUnit.SECONDS.sleep(2);

        PutRecordRequest putRecordRequest = PutRecordRequest.builder()
                .partitionKey("partitionkey")
                .streamName(streamName)
                .data(SdkBytes.fromUtf8String("hello, world!"))
                .build();
        String shardId = kinesisClient.putRecord(putRecordRequest).get().shardId();

        GetShardIteratorRequest getShardIteratorRequest = GetShardIteratorRequest.builder()
                .shardId(shardId)
                .shardIteratorType(ShardIteratorType.TRIM_HORIZON)
                .streamName(streamName)
                .build();
        String shardIterator = kinesisClient
                .getShardIterator(getShardIteratorRequest)
                .get()
                .shardIterator();

        GetRecordsRequest getRecordRequest = GetRecordsRequest.builder().shardIterator(shardIterator).build();
        Integer limit = 100;
        Integer counter = 0;
        Boolean recordFound = false;

        while (true) {
            GetRecordsResponse recordsResponse = kinesisClient.getRecords(getRecordRequest).get();

            if (recordsResponse.hasRecords()) {
                recordFound = true;
                break;
            }

            if(counter >= limit){
                break;
            }

            counter += 1;
            shardIterator = recordsResponse.nextShardIterator();
        }
        Assert.assertTrue(recordFound);
    }
}
