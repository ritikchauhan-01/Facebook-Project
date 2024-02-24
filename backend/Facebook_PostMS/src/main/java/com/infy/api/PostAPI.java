package com.infy.api;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.infy.dto.PostDTO;
import com.infy.dto.UserDTO;
import com.infy.exception.FacebookException;
import com.infy.model.UserFile;
import com.infy.service.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "post-api")
@Validated
@CrossOrigin
public class PostAPI {
	@Autowired
	private PostService postService;

	public static final Log LOGGER = LogFactory.getLog(PostAPI.class);

	@GetMapping(value = "/post/{postId}")
	public ResponseEntity<PostDTO> getPostById(@PathVariable String postId) throws FacebookException {
		return new ResponseEntity<>(postService.getPostById(postId), HttpStatus.OK);
	}

	@GetMapping(value = "/posts/{userId}/{pageNumber}/{pageSize}")
	public ResponseEntity<List<PostDTO>> getAllPosts(@PathVariable String userId, @PathVariable Integer pageNumber,
			@PathVariable Integer pageSize) throws FacebookException {
		return new ResponseEntity<>(postService.getAllPosts(userId, pageNumber, pageSize), HttpStatus.OK);
	}

	@PostMapping("/post")
	public ResponseEntity<PostDTO> createPost(@RequestHeader("userId") String userId,
			@Valid @RequestBody PostDTO postDTO) throws Exception {
		UserDTO uDto = new UserDTO();
		uDto.setUserId(userId);
		postDTO.setUser(uDto);
		return new ResponseEntity<>(postService.createPost(postDTO), HttpStatus.CREATED);
	}

	@PostMapping("/post/postImage/{postId}")
	public ResponseEntity<PostDTO> upload(@PathVariable String postId,
			@RequestParam("postImage") MultipartFile postImage) throws Exception {
		return new ResponseEntity<>(postService.uploadPostImage(postId, postImage), HttpStatus.OK);
	}

	@GetMapping("/post/postImage/{postId}")
	public ResponseEntity<ByteArrayResource> download(@PathVariable String postId) throws Exception {
		UserFile postImage = postService.downloadPostImage(postId);

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(postImage.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + postImage.getFilename() + "\"")
				.body(new ByteArrayResource(postImage.getFile()));
	}

	@GetMapping(value = "/posts/search")
	public ResponseEntity<List<PostDTO>> searchPosts(@RequestParam String keyword) throws FacebookException {
		return new ResponseEntity<>(postService.searchPosts(keyword), HttpStatus.OK);
	}
}
