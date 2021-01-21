package com.kinesis.poc.consumer;

import com.kinesis.poc.events.Incoming.AwsTrackedDeliveredMessageStatus;
import com.kinesis.poc.events.outgoing.SmsNotificationDeliveryTrackingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import software.amazon.kinesis.exceptions.InvalidStateException;
import software.amazon.kinesis.exceptions.ShutdownException;
import software.amazon.kinesis.lifecycle.events.*;
import software.amazon.kinesis.processor.ShardRecordProcessor;
import software.amazon.kinesis.retrieval.KinesisClientRecord;

import java.io.IOException;

public class DeliveryStatusProcessor implements ShardRecordProcessor {

    private static final String SHARD_ID_MDC_KEY = "ShardId";
    private final EventProcessor eventProcessor;

    private static final Logger log = LoggerFactory.getLogger(DeliveryStatusProcessor.class);
    private String shardId;

    public DeliveryStatusProcessor(EventProcessor eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    public void initialize(InitializationInput initializationInput) {
        shardId = initializationInput.shardId();
        MDC.put(SHARD_ID_MDC_KEY, shardId);
        try {
            log.info("Initializing @ Sequence: {}", initializationInput.extendedSequenceNumber());
        } finally {
            MDC.remove(SHARD_ID_MDC_KEY);
        }
    }

    public void processRecords(ProcessRecordsInput processRecordsInput) {
        MDC.put(SHARD_ID_MDC_KEY, shardId);
        try {
            log.info("Processing {} record(s)", processRecordsInput.records().size());
            processRecordsInput.records().forEach(incomingDeliveryStatus ->
            {
                try {
                    processRecord(incomingDeliveryStatus);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Throwable t) {
            log.error("Caught throwable while processing records. Aborting.");
            Runtime.getRuntime().halt(1);
        } finally {
            MDC.remove(SHARD_ID_MDC_KEY);
        }
    }

    public void processRecord(KinesisClientRecord record) throws IOException {
        byte[] messageStatus = new byte[record.data().remaining()];
        record.data().get(messageStatus);
        String string = new String(messageStatus);
        System.out.println(string);
      // processAndPublishRecord(messageStatus);
    }

    public void processAndPublishRecord(byte[] messageStatus) throws IOException {
        AwsTrackedDeliveredMessageStatus awsTrackedDeliveredMessageStatus = eventProcessor.mapMessageStatusToAwsTrackedDeliveredMessageStatus(messageStatus);
        SmsNotificationDeliveryTrackingEvent smsNotificationDeliveryTrackingEvent = eventProcessor.mapAwsTrackedDeliveredMessageStatusToSmsNotificationDeliveryTrackingEvent(awsTrackedDeliveredMessageStatus);
        System.out.println(smsNotificationDeliveryTrackingEvent.getMessageId());
        // TODO : implement your logic here
    }

    @Override
    public void leaseLost(LeaseLostInput leaseLostInput) {
        MDC.put(SHARD_ID_MDC_KEY, shardId);
        log.info("Lost lease, so terminating.");
    }

    public void shardEnded(ShardEndedInput shardEndedInput) {
        MDC.put(SHARD_ID_MDC_KEY, shardId);
        try {
            log.info("Reached shard end checkpointing.");
            shardEndedInput.checkpointer().checkpoint();
        } catch (ShutdownException | InvalidStateException e) {
            log.error("Exception while checkpointing at shard end. Giving up.", e);
        } finally {
            MDC.remove(SHARD_ID_MDC_KEY);
        }
    }

    public void shutdownRequested(ShutdownRequestedInput shutdownRequestedInput) {
        MDC.put(SHARD_ID_MDC_KEY, shardId);
        try {
            log.info("Scheduler is shutting down, checkpointing.");
            shutdownRequestedInput.checkpointer().checkpoint();
        } catch (ShutdownException | InvalidStateException e) {
            log.error("Exception while checkpointing at requested shutdown. Giving up.", e);
        } finally {
            MDC.remove(SHARD_ID_MDC_KEY);
        }
    }
}

