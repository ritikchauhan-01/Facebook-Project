package com.infy;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

	@Value(value = "${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Value(value = "${message.topic.friend.name}")
	private String topicName1;

	@Value(value = "${message.topic.post.name}")
	private String topicName2;

	@Value(value = "${message.topic.user.name}")
	private String topicName3;

	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		return new KafkaAdmin(configs);
	}

	@Bean
	public NewTopic topic1() {
		return new NewTopic(topicName1, 1, (short) 1);
	}

	@Bean
	public NewTopic topic2() {
		return new NewTopic(topicName2, 1, (short) 1);
	}

	@Bean
	public NewTopic topic3() {
		return new NewTopic(topicName3, 1, (short) 1);
	}
}
