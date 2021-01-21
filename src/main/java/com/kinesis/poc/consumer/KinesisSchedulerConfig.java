package com.kinesis.poc.consumer;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.kinesis.common.ConfigsBuilder;
import software.amazon.kinesis.common.KinesisClientUtil;
import software.amazon.kinesis.coordinator.Scheduler;
import software.amazon.kinesis.metrics.NullMetricsFactory;

@Component
public class KinesisSchedulerConfig {

    public Scheduler createScheduler(ConfigsBuilder configsBuilder) {
        return new Scheduler(
                configsBuilder.checkpointConfig(),
                configsBuilder.coordinatorConfig(),
                configsBuilder.leaseManagementConfig(),
                configsBuilder.lifecycleConfig(),
                configsBuilder.metricsConfig().metricsFactory(new NullMetricsFactory()),
                configsBuilder.processorConfig(),
                configsBuilder.retrievalConfig()
        );
    }

    public KinesisAsyncClient createKinesisClient(Region region) {
        return KinesisClientUtil.createKinesisAsyncClient(KinesisAsyncClient.builder()
                .region(region));
    }

    public DynamoDbAsyncClient createDynamoClient(Region region) {
        return DynamoDbAsyncClient.builder().region(region)
                .build();
    }

    public CloudWatchAsyncClient createCloudWatchAsyncClient(Region region) {
        return CloudWatchAsyncClient.builder().region(region)
                .build();
    }

}
