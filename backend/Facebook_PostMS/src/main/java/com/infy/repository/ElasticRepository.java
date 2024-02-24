package com.infy.repository;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.infy.model.ElasticPost;

public interface ElasticRepository extends ElasticsearchRepository<ElasticPost, String> {

//	@Query("{\"bool\": {\"should\": [{\"match\": {\"description\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}} ]}}")
//	@Query("{\"match\":{\"description\":\"?0\"}}")
	@Query("{\"regexp\":{\"description\":{\"value\":\".*?0.*\",\"flags\":\"ALL\",\"case_insensitive\":true,\"max_determinized_states\":10000,\"rewrite\":\"constant_score\"}}}")
	List<ElasticPost> searchPosts(String keyword);
}
