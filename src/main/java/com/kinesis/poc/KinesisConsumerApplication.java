package com.kinesis.poc;

import com.kinesis.poc.consumer.KinesisScheduler;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties
public class KinesisConsumerApplication implements ApplicationRunner {

	private final KinesisScheduler kinesisScheduler;

	public KinesisConsumerApplication(KinesisScheduler kinesisScheduler) {
		this.kinesisScheduler = kinesisScheduler;
	}

	public static void main(String[] args) {
		SpringApplication.run(KinesisConsumerApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
			kinesisScheduler.run();
	}
}
