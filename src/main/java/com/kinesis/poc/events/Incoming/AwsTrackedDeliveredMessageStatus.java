package com.kinesis.poc.events.Incoming;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class AwsTrackedDeliveredMessageStatus {

    private final static ObjectMapper JSON = new ObjectMapper();
    static {
        JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private Attributes attributes;

    public AwsTrackedDeliveredMessageStatus() {}

    public AwsTrackedDeliveredMessageStatus(Attributes attributes) {
        this.attributes = attributes;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public static AwsTrackedDeliveredMessageStatus mapJsonToBytes(byte[] message) throws IOException {
        return JSON.readValue(message, AwsTrackedDeliveredMessageStatus.class);
    }

    @Override
    public String toString() {
        return String.format("attributes", attributes);
    }
}