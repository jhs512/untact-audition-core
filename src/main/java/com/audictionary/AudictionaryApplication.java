package com.audictionary;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class AudictionaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(AudictionaryApplication.class, args);
	}

}
