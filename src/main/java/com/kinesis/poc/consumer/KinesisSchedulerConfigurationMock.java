package com.kinesis.poc.consumer;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.kinesis.common.ConfigsBuilder;
import software.amazon.kinesis.common.KinesisClientUtil;
import software.amazon.kinesis.coordinator.Scheduler;

import java.util.UUID;

@Component
public class KinesisSchedulerConfigurationMock extends KinesisConfiguration {

    private final DeliveryStatusRecordProcessorFactory deliveryStatusRecordProcessorFactory;

    public KinesisSchedulerConfigurationMock(DeliveryStatusRecordProcessorFactory deliveryStatusRecordProcessorFactory) {
        this.deliveryStatusRecordProcessorFactory = deliveryStatusRecordProcessorFactory;
    }

    @Override
    public void run() {
        System.out.println("mocked object in progress");
    }

    @Override
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

    @Override
    public KinesisAsyncClient createKinesisClient(Region region) {
        return KinesisClientUtil.createKinesisAsyncClient(KinesisAsyncClient.builder().region(region));
    }

    @Override
    public DynamoDbAsyncClient createDynamoClient(Region region) {
        return DynamoDbAsyncClient.builder().region(region).build();
    }

    @Override
    public CloudWatchAsyncClient createCloudWatchAsyncClient(Region region) {
        return CloudWatchAsyncClient.builder().region(region).build();
    }

}