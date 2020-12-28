package com.kinesis.poc.medium;

import com.kinesis.poc.consumer.DeliveryStatusRecordProcessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.kinesis.common.ConfigsBuilder;
import software.amazon.kinesis.coordinator.Scheduler;

import java.util.UUID;

@TestConfiguration
public class AWSConfig implements Runnable {

    private final DeliveryStatusRecordProcessorFactory deliveryStatusRecordProcessorFactory;

    public AWSConfig(DeliveryStatusRecordProcessorFactory deliveryStatusRecordProcessorFactory) {
        this.deliveryStatusRecordProcessorFactory = deliveryStatusRecordProcessorFactory;
    }

    @Autowired
    private AwsKinesisMock awsKinesisMock;

    public void run() {

        String applicationName = "test";
        String streamName = "test";

        KinesisAsyncClient kinesisClient = awsKinesisMock.getKinesisMock();
        DynamoDbAsyncClient dynamoClient = awsKinesisMock.getDynamoDbAsyncClient();
        CloudWatchAsyncClient cloudWatchClient = awsKinesisMock.getCloudWatchAsyncClient();
        ConfigsBuilder configsBuilder = new ConfigsBuilder(streamName, applicationName, kinesisClient, dynamoClient, cloudWatchClient, UUID.randomUUID().toString(), deliveryStatusRecordProcessorFactory);

        Scheduler scheduler = createScheduler(configsBuilder);
        scheduler.run();
    }

    public Scheduler createScheduler(ConfigsBuilder configsBuilder) {
        return new Scheduler(
                configsBuilder.checkpointConfig(),
                configsBuilder.coordinatorConfig(),
                configsBuilder.leaseManagementConfig(),
                configsBuilder.lifecycleConfig(),
                configsBuilder.metricsConfig(),
                configsBuilder.processorConfig(),
                configsBuilder.retrievalConfig()
        );
    }

}
