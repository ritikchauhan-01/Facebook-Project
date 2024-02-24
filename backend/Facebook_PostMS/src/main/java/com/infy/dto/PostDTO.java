package com.infy.dto;

import java.time.LocalDateTime;

import com.infy.model.Post;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;

public class PostDTO {

	private String postId;
	private String description;
	private String postImageId;
	private Integer likes;

	@PastOrPresent(message = "{post.postTime.invalid}")
	private LocalDateTime postTime;

	@Valid
	private UserDTO user;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPostImageId() {
		return postImageId;
	}

	public void setPostImageId(String postImageId) {
		this.postImageId = postImageId;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public LocalDateTime getPostTime() {
		return postTime;
	}

	public void setPostTime(LocalDateTime postTime) {
		this.postTime = postTime;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public static Post toPost(PostDTO dto) {
		Post p = new Post();

		p.setDescription(dto.getDescription());
		p.setLikes(dto.getLikes());
		p.setPostImageId(dto.getPostImageId());
		p.setPostTime(LocalDateTime.now());

		if (dto.getUser() != null && dto.getUser().getUserId() != null)
			p.setUserId(dto.getUser().getUserId());

		return p;
	}
}
