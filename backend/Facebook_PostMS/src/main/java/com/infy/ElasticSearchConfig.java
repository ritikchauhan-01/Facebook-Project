package com.infy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.infy.repository")
public class ElasticSearchConfig extends ElasticsearchConfiguration {

	@Value(value = "${eSearch.address}")
	private String address;

	@Value(value = "${eSearch.user.name}")
	private String userName;

	@Value(value = "${eSearch.user.password}")
	private String password;

	@Value(value = "${eSearch.ca-fingerprint}")
	private String caFingerprint;

	@Override
	public ClientConfiguration clientConfiguration() {

		return ClientConfiguration.builder().connectedTo(address).usingSsl(caFingerprint)
				.withBasicAuth(userName, password).build();
	}

}
