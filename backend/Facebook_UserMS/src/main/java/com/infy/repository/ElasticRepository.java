package com.infy.repository;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.infy.model.ElasticUser;

public interface ElasticRepository extends ElasticsearchRepository<ElasticUser, String> {

//	@Query("{\"bool\": {\"should\": [{\"match\": {\"userName\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}, {\"match\": {\"fullName\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}} ]}}")
	@Query("{\"regexp\":{\"userName\":{\"value\":\".*?0.*\",\"flags\":\"ALL\",\"case_insensitive\":true,\"max_determinized_states\":10000,\"rewrite\":\"constant_score\"}}}")
	List<ElasticUser> searchUsers(String keyword);
}
