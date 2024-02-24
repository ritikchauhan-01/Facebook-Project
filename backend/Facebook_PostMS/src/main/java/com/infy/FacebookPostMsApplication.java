package com.infy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableDiscoveryClient
@PropertySource(value = { "classpath:messages.properties" })
public class FacebookPostMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FacebookPostMsApplication.class, args);
	}

}
