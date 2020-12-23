package com.kinesis.poc.consumer;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.kinesis.common.ConfigsBuilder;
import software.amazon.kinesis.coordinator.Scheduler;

public abstract class KinesisConfiguration implements Runnable  {

    public abstract Scheduler createScheduler(ConfigsBuilder configsBuilder);

    public abstract KinesisAsyncClient createKinesisClient(Region region);

    public abstract DynamoDbAsyncClient createDynamoClient(Region region);

    public abstract CloudWatchAsyncClient createCloudWatchAsyncClient(Region region);

}
