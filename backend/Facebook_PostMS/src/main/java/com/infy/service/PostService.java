package com.infy.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.infy.dto.PostDTO;
import com.infy.exception.FacebookException;
import com.infy.model.UserFile;

public interface PostService {
	PostDTO getPostById(String postId) throws FacebookException;

	List<PostDTO> getAllPosts(String userId, Integer pageNumber, Integer pageSize) throws FacebookException;

	PostDTO createPost(PostDTO postDTO) throws FacebookException;

	PostDTO uploadPostImage(String postId, MultipartFile upload) throws FacebookException, IOException;

	UserFile downloadPostImage(String postId) throws FacebookException, IOException;

	List<PostDTO> searchPosts(String keyword) throws FacebookException;

}
