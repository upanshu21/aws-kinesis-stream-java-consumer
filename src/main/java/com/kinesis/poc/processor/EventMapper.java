package com.kinesis.poc.processor;

import com.kinesis.poc.events.Incoming.AwsTrackedDeliveredMessageStatus;
import com.kinesis.poc.events.outgoing.SmsNotificationDeliveryTrackingEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EventMapper {

    public AwsTrackedDeliveredMessageStatus mapMessageStatusToAwsTrackedDeliveredMessageStatus(byte[] messageStatus) throws IOException {
        return AwsTrackedDeliveredMessageStatus.mapJsonToBytes(messageStatus);
    }

    public SmsNotificationDeliveryTrackingEvent mapAwsTrackedDeliveredMessageStatusToSmsNotificationDeliveryTrackingEvent(
            AwsTrackedDeliveredMessageStatus awsTrackedDeliveredMessageStatus) {
        return new SmsNotificationDeliveryTrackingEvent.Builder()
                .withMessageDeliveryStatus(awsTrackedDeliveredMessageStatus.getAttributes().getRecordStatus())
                .withMessageId(awsTrackedDeliveredMessageStatus.getAttributes().getMessageId())
                .build();
    }
}
