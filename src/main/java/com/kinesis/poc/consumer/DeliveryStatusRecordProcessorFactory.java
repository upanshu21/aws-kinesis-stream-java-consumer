package com.kinesis.poc.consumer;

import com.kinesis.poc.processor.EventProcessor;
import org.springframework.stereotype.Component;
import software.amazon.kinesis.processor.ShardRecordProcessor;
import software.amazon.kinesis.processor.ShardRecordProcessorFactory;

@Component
public class DeliveryStatusRecordProcessorFactory implements ShardRecordProcessorFactory {

    private final EventProcessor eventProcessor;
    public DeliveryStatusRecordProcessorFactory(EventProcessor eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    public ShardRecordProcessor shardRecordProcessor() {
        return new DeliveryStatusProcessor(eventProcessor);
    }
}