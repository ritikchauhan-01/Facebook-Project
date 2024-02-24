package com.infy;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.infy.datacache.CacheStore;
import com.infy.dto.PostDTO;

@Configuration
public class PostConfig {
	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public CacheStore<PostDTO> postCache() {
		return new CacheStore<>(120);
	}
}
