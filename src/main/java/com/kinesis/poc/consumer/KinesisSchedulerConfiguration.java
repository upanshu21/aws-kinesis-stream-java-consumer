package com.kinesis.poc.consumer;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.kinesis.common.ConfigsBuilder;
import software.amazon.kinesis.common.KinesisClientUtil;
import software.amazon.kinesis.coordinator.Scheduler;
import software.amazon.kinesis.metrics.NullMetricsFactory;
import software.amazon.kinesis.retrieval.polling.PollingConfig;

import java.net.URI;
import java.util.UUID;

@Component
public class KinesisSchedulerConfiguration implements Runnable{

    private final DeliveryStatusRecordProcessorFactory deliveryStatusRecordProcessorFactory;

    public KinesisSchedulerConfiguration(DeliveryStatusRecordProcessorFactory deliveryStatusRecordProcessorFactory) {
        this.deliveryStatusRecordProcessorFactory = deliveryStatusRecordProcessorFactory;
    }

    public void run() {

        String applicationName = "test";
        String streamName = "test";
        Region region = Region.of("us-east-1");


        KinesisAsyncClient kinesisClient = createKinesisClient(region);
        DynamoDbAsyncClient dynamoClient = createDynamoClient(region);
        CloudWatchAsyncClient cloudWatchClient = createCloudWatchAsyncClient(region);
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
                configsBuilder.metricsConfig().metricsFactory(new NullMetricsFactory()),
                configsBuilder.processorConfig(),
                configsBuilder.retrievalConfig()
        );
    }

    public KinesisAsyncClient createKinesisClient(Region region) {
        return KinesisClientUtil.createKinesisAsyncClient(KinesisAsyncClient.builder()
                .credentialsProvider(() -> AwsBasicCredentials.create("test","test"))
                .endpointOverride(URI.create("http://localhost:4566"))
                .region(region));
    }

    public DynamoDbAsyncClient createDynamoClient(Region region) {
        return DynamoDbAsyncClient.builder().region(region)
                .credentialsProvider(() -> AwsBasicCredentials.create("test","test"))
                .endpointOverride(URI.create("http://localhost:4566"))
                .build();
    }

    public CloudWatchAsyncClient createCloudWatchAsyncClient(Region region) {
        return CloudWatchAsyncClient.builder().region(region)
                .credentialsProvider(() -> AwsBasicCredentials.create("test","test"))
                .endpointOverride(URI.create("http://localhost:4566"))
                .build();

    }

}