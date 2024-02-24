package com.infy.model;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.infy.dto.PostDTO;

@Document(collection = "post")
public class Post {

	@Id
	private String postId;
	private ObjectId userId;
	private String description;
	private ObjectId postImageId;
	private Integer likes;

	@CreatedDate
	private LocalDateTime postTime;

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getUserId() {
		if (userId == null)
			return null;
		return userId.toString();
	}

	public void setUserId(String userId) {
		if (userId != null)
			this.userId = new ObjectId(userId);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPostImageId() {
		if (postImageId == null)
			return null;
		return postImageId.toString();
	}

	public void setPostImageId(String postImageId) {
		if (postImageId != null)
			this.postImageId = new ObjectId(postImageId);
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

	public static PostDTO toPostDTO(Post p) {
		PostDTO dto = new PostDTO();

		dto.setPostId(p.getPostId());
		dto.setDescription(p.getDescription());
		dto.setLikes(p.getLikes());
		dto.setPostImageId(p.getPostImageId());
		dto.setPostTime(p.getPostTime());

		return dto;
	}

	public static ElasticPost toElasticPost(Post p) {
		ElasticPost ePost = new ElasticPost();

		ePost.setPostId(p.getPostId());
		ePost.setDescription(p.getDescription());

		return ePost;
	}
}
