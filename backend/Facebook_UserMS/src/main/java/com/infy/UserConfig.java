package com.infy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.infy.datacache.CacheStore;
import com.infy.dto.UserDTO;

@Configuration
public class UserConfig {

	@Bean
	public CacheStore<UserDTO> userCache() {
		return new CacheStore<>(120);
	}

}
