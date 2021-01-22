package com.kinesis.poc.processor;

import com.kinesis.poc.events.Incoming.AwsTrackedDeliveredMessageStatus;
import com.kinesis.poc.events.outgoing.SmsNotificationDeliveryTrackingEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EventProcessor {

    private final EventMapper eventMapper;

    public EventProcessor(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    public void processAndPublishRecord(byte[] messageStatus) throws IOException {
        AwsTrackedDeliveredMessageStatus awsTrackedDeliveredMessageStatus = eventMapper.mapMessageStatusToAwsTrackedDeliveredMessageStatus(messageStatus);
        SmsNotificationDeliveryTrackingEvent smsNotificationDeliveryTrackingEvent = eventMapper.mapAwsTrackedDeliveredMessageStatusToSmsNotificationDeliveryTrackingEvent(awsTrackedDeliveredMessageStatus);
        System.out.println(smsNotificationDeliveryTrackingEvent.getMessageId());
        // TODO : implement your logic here
    }
}
