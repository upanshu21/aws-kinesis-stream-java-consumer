package com.kinesis.poc;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.kinesis.poc.consumer.KinesisScheduler;
import org.json.JSONObject;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.nio.ByteBuffer;

@SpringBootApplication
@EnableConfigurationProperties
public class KinesisConsumerApplication implements ApplicationRunner {

	private final KinesisScheduler kinesisScheduler;

	public KinesisConsumerApplication(KinesisScheduler kinesisScheduler) {
		this.kinesisScheduler = kinesisScheduler;
	}


	public static void main(String[] args) {
		SpringApplication.run(KinesisConsumerApplication.class, args);

//		AmazonKinesisClientBuilder clientBuilder = AmazonKinesisClientBuilder.standard();
//		AmazonKinesis amazonKinesis = clientBuilder.build();
//		clientBuilder.build();
//		//amazonKinesis.createStream("test", 1);
//
//		JSONObject internalJson = new JSONObject();
//		JSONObject jsonObject = new JSONObject();
//		internalJson.put("recordStatus", "successful!!!!!!");
//		internalJson.put("messageId", "1234");
//
//		jsonObject.put("attributes", internalJson);
//		byte[] a = jsonObject.toString().getBytes();
//
//		PutRecordRequest putRecordRequest = new PutRecordRequest()
//				.withStreamName("test1")
//				.withPartitionKey("abc")
//				.withData(ByteBuffer.wrap(a));
//
//		System.out.println(amazonKinesis.putRecord(putRecordRequest).getSequenceNumber());
//


	}
	@Override
	public void run(ApplicationArguments args) {
			kinesisScheduler.run();
	}
}
