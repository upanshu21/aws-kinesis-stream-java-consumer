package com.kinesis.poc.medium;

//import com.amazonaws.services.kinesis.AmazonKinesis;
//import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.kinesis.poc.KinesisConsumerApplication;
import com.kinesis.poc.consumer.EventProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {KinesisConsumerApplication.class, AWSConfig.class})
@ExtendWith(SpringExtension.class)
public class MediumTests {

    @Autowired
    private EventProcessor eventProcessor;

//    private AmazonKinesis amazonKinesis;

    @Autowired
    private AwsKinesisMock awsKinesisMock;

    @Autowired
    private AWSConfig awsConfig;

    @BeforeEach
    public void init() {
//       // amazonKinesis = awsKinesisMock.publishDataToKinesis();
//        amazonKinesis.createStream("test", 1);
//        PutRecordRequest putRecordRequest = new PutRecordRequest()
//                .withStreamName("test")
//                .withPartitionKey("abc")
//                .withData(ByteBuffer.wrap("hello".getBytes()));
//
//        amazonKinesis.putRecord(putRecordRequest);
    }


    @Test
    public void check() throws IOException {
        String message = "hello";
        byte[] arr = message.getBytes();
        verify(eventProcessor, timeout(30000)).mapMessageStatusToAwsTrackedDeliveredMessageStatus(arr);

    }

}
