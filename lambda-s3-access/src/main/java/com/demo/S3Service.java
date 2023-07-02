package com.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.demo.mode.UserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class S3Service {

	private static final Logger LOGGER = LoggerFactory.getLogger(S3Service.class);

	@Autowired
	private AmazonS3 s3Client;

	private ObjectMapper mapper = new ObjectMapper();

	public List<String> getBuckets() {

		List<String> bucketNames = s3Client.listBuckets().stream().map(b -> b.getName()).collect(Collectors.toList());
		return bucketNames;
	}

	public String create(String userInfoStr) {
		try {
			UserInfo userInfo = mapper.readValue(userInfoStr, UserInfo.class);

			PutObjectResult putObjectResult = s3Client.putObject(userInfo.getBucketName(), userInfo.getId(),
					userInfo.getContent());

			LOGGER.info(" getRawMetadata = " + putObjectResult.getMetadata().getRawMetadata());

			return putObjectResult.getMetadata().getETag();
		} catch (Exception e) {
			LOGGER.error("error", e);
			throw new RuntimeException(e);
		}

	}

	public String get(String userInfoStr) {
		String content = null;
		try {
			UserInfo userInfo = mapper.readValue(userInfoStr, UserInfo.class);

			S3Object s3Object = s3Client.getObject(userInfo.getBucketName(), userInfo.getId());

			LOGGER.info(" s3Object = " + s3Object);

			S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();

			content = StreamUtils.copyToString(s3ObjectInputStream, StandardCharsets.UTF_8);
		} catch (IOException e) {
			LOGGER.error("error", e);
			throw new RuntimeException(e);
		}

		return content;
	}

}
