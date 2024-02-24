package com.infy.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.infy.dal.PostDAL;
import com.infy.datacache.CacheStore;
import com.infy.dto.PostDTO;
import com.infy.dto.UserDTO;
import com.infy.exception.FacebookException;
import com.infy.model.ElasticPost;
import com.infy.model.Event;
import com.infy.model.Post;
import com.infy.model.Type;
import com.infy.model.UserFile;
import com.infy.repository.ElasticRepository;

@Service(value = "postService")
@Transactional
public class PostServiceImpl implements PostService {

	@Autowired
	CacheStore<PostDTO> postCache;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PostDAL postDAL;

	@Autowired
	private KafkaTemplate<String, Event> kafkaTemplate;

	@Autowired
	private ElasticRepository elasticRepository;

	@Value(value = "${message.topic.post.name}")
	private String topicName;

	private static final String USER_URL = "http://Facebook-UserMS/facebook/user-api/user/";

	@Override
	public PostDTO getPostById(String postId) throws FacebookException {
		PostDTO cachedDto = postCache.get(postId);
		if (cachedDto != null)
			return cachedDto;

		Post post = postDAL.findById(postId);
		if (post == null)
			throw new FacebookException("PostService.INVALID_POSTID");

		UserDTO userDTO = restTemplate.getForObject(USER_URL + post.getUserId(), UserDTO.class);

		PostDTO dto = Post.toPostDTO(post);
		dto.setUser(userDTO);
		postCache.add(dto.getPostId(), dto);

		return dto;
	}

	@Override
	public List<PostDTO> getAllPosts(String userId, Integer pageNumber, Integer pageSize) throws FacebookException {
		UserDTO userDTO = restTemplate.getForObject(USER_URL + userId, UserDTO.class);

		List<Post> posts = postDAL.findAllByUserId(userDTO.getUserId(), pageNumber, pageSize);
		List<PostDTO> postDTOs = new ArrayList<>();

		for (Post p : posts) {
			PostDTO dto = Post.toPostDTO(p);
			dto.setUser(userDTO);
			postDTOs.add(dto);
		}
		return postDTOs;
	}

	@Override
	public PostDTO createPost(PostDTO postDTO) throws FacebookException {
		if (postDTO.getUser() == null || postDTO.getUser().getUserId() == null)
			throw new FacebookException("PostService.INVALID_USERID");

		UserDTO userDTO = restTemplate.getForObject(USER_URL + postDTO.getUser().getUserId(), UserDTO.class);

		Post post = postDAL.savePost(PostDTO.toPost(postDTO));

		elasticRepository.save(Post.toElasticPost(post));

		PostDTO dto = Post.toPostDTO(post);
		dto.setUser(userDTO);
		postCache.add(dto.getPostId(), dto);

		kafkaTemplate.send(topicName, new Event(postDTO.getUser().getUserId(), post.getPostId(), Type.P_CREATED));

		return dto;
	}

	@Override
	public PostDTO uploadPostImage(String postId, MultipartFile upload) throws FacebookException, IOException {
		Post post = postDAL.findById(postId);
		if (post == null)
			throw new FacebookException("PostService.INVALID_POSTID");

		ObjectId postImageId = postDAL.uploadFile(upload);

		Post tempPost = new Post();
		tempPost.setPostId(post.getPostId());
		tempPost.setPostImageId(postImageId.toString());
		Post updatedPost = postDAL.findByIdAndUpdate(tempPost);

		PostDTO dto = Post.toPostDTO(updatedPost);
		postCache.add(dto.getPostId(), dto);

		return dto;
	}

	@Override
	public UserFile downloadPostImage(String postId) throws FacebookException, IOException {
		PostDTO dto = postCache.get(postId);
		if (dto == null) {
			Post post = postDAL.findById(postId);
			if (post == null)
				throw new FacebookException("PostService.INVALID_POSTID");
			dto = Post.toPostDTO(post);
		}

		if (dto.getPostImageId() == null || dto.getPostImageId().isBlank())
			throw new FacebookException("PostService.INVALID_POST_IMAGEID");

		return postDAL.downloadFile(dto.getPostImageId());
	}

	@Override
	public List<PostDTO> searchPosts(String keyword) throws FacebookException {
		List<ElasticPost> ePosts = elasticRepository.searchPosts(keyword);
		List<ObjectId> ids = new ArrayList<>();

		for (ElasticPost ep : ePosts)
			ids.add(new ObjectId(ep.getPostId()));

		List<Post> posts = postDAL.findByIds(ids);
		List<PostDTO> dtos = new ArrayList<>();

		for (Post p : posts) {
			UserDTO userDTO = restTemplate.getForObject(USER_URL + p.getUserId(), UserDTO.class);
			PostDTO dto = Post.toPostDTO(p);
			dto.setUser(userDTO);
			dtos.add(dto);
		}

		return dtos;
	}

}
