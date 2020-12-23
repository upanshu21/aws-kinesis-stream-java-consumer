package com.kinesis.poc.events.outgoing;

public class SmsNotificationDeliveryTrackingEvent {

    private final String messageDeliveryStatus;
    private final String messageId;

    private SmsNotificationDeliveryTrackingEvent(Builder builder) {
        messageDeliveryStatus = builder.messageDeliveryStatus;
        messageId = builder.messageId;
    }

    public String getMessageDeliveryStatus() {
        return messageDeliveryStatus;
    }

    public String getMessageId() {
        return messageId;
    }

    public static class Builder {
        private String messageDeliveryStatus;
        private String messageId;

        public Builder withMessageDeliveryStatus(String messageDeliveryStatus){
            this.messageDeliveryStatus = messageDeliveryStatus;
            return this;
        }

        public Builder withMessageId(String messageId){
            this.messageId = messageId;
            return this;
        }

        public SmsNotificationDeliveryTrackingEvent build() {
            return new SmsNotificationDeliveryTrackingEvent(this);
        }
    }

}