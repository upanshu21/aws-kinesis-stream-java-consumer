package com.kinesis.poc;

import com.kinesis.poc.consumer.KinesisConfiguration;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class KinesisConsumerApplication implements ApplicationRunner {

	private final KinesisConfiguration kinesisConfiguration;

	public KinesisConsumerApplication(KinesisConfiguration kinesisConfiguration) {
		this.kinesisConfiguration = kinesisConfiguration;
	}


	public static void main(String[] args) {
		SpringApplication.run(KinesisConsumerApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		kinesisConfiguration.run();
	}

}
