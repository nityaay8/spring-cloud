package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@SpringBootApplication
public class S3AccessApplication {

	public static void main(String[] args) {
		SpringApplication.run(S3AccessApplication.class, args);
	}

	@Bean
	public AmazonS3 getAmazonS3Client() {

		// Get Amazon S3 client and return the S3 client object
		return AmazonS3ClientBuilder.standard().withRegion("us-east-1").build();
	}

}
