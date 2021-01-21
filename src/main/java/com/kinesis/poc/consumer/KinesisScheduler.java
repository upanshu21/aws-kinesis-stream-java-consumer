package com.kinesis.poc.consumer;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.kinesis.common.ConfigsBuilder;
import software.amazon.kinesis.coordinator.Scheduler;

import java.util.UUID;

@Component
public class KinesisScheduler implements Runnable{

    private final DeliveryStatusRecordProcessorFactory deliveryStatusRecordProcessorFactory;
    private final KinesisSchedulerConfig kinesisSchedulerConfig;

    public KinesisScheduler(DeliveryStatusRecordProcessorFactory deliveryStatusRecordProcessorFactory, KinesisSchedulerConfig kinesisSchedulerConfig) {
        this.deliveryStatusRecordProcessorFactory = deliveryStatusRecordProcessorFactory;
        this.kinesisSchedulerConfig = kinesisSchedulerConfig;
    }

    public void run() {

        String applicationName = "test";
        String streamName = "test";
        Region region = Region.of("us-east-1");

        KinesisAsyncClient kinesisClient = kinesisSchedulerConfig.createKinesisClient(region);
        DynamoDbAsyncClient dynamoClient = kinesisSchedulerConfig.createDynamoClient(region);
        CloudWatchAsyncClient cloudWatchClient = kinesisSchedulerConfig.createCloudWatchAsyncClient(region);
        ConfigsBuilder configsBuilder = new ConfigsBuilder(streamName, applicationName, kinesisClient, dynamoClient, cloudWatchClient, UUID.randomUUID().toString(), deliveryStatusRecordProcessorFactory);

        Scheduler scheduler = kinesisSchedulerConfig.createScheduler(configsBuilder);
        scheduler.run();
    }

}