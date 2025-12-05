package com.QuantaFeed.marketstream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QuantaFeedApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuantaFeedApplication.class, args);

	}

}
