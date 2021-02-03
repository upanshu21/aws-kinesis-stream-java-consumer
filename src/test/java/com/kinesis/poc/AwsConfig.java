package com.kinesis.poc;

import cloud.localstack.awssdkv2.TestUtils;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;

@Component
public class AwsConfig {

    public KinesisAsyncClient kinesisClient() {
        return TestUtils.getClientKinesisAsyncV2();
    }

}
