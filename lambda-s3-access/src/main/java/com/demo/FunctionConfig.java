package com.demo;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.MessageRoutingCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

@Configuration
public class FunctionConfig {

	@Autowired
	private S3Service s3Service;

	@Bean
	public MessageRoutingCallback customRouter() {
		return new MessageRoutingCallback() {
			@Override
			public String routingResult(Message<?> message) {
				return (String) message.getHeaders().get("func_name");
			}
		};
	}

	@Bean
	public Function<String, String> uppercase() {
		return value -> value.toUpperCase();
	}

	@Bean
	public Function<String, List<String>> listBuckets() {
		return value -> s3Service.getBuckets();
	}

	@Bean
	public Function<String, String> createUserContent() {
		return value -> s3Service.create(value);
	}

	@Bean
	public Function<String, String> getUserContent() {
		return value -> s3Service.get(value);
	}

	@Bean
	public Function<String, String> reverse1() {
		return value -> value.chars().mapToObj(c -> Character.valueOf((char) c))
				.reduce("", (a, b) -> b + a, (a1, b1) -> b1 + "" + a1).toString();
	}

	@Bean
	public Function<String, String> reverse2() {
		return value -> {

			final int len = value.length();
			final char[] ch = value.toCharArray();

			String revStr = IntStream.range(0, len).mapToObj(i -> ch[len - i - 1])
					.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();

			return revStr;
		};
	}

}
