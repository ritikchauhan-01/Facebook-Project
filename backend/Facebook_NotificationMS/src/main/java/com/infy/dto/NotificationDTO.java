package com.infy.dto;

import java.time.LocalDateTime;

public class NotificationDTO {

	private String notificationId;
	private String type;
	private String description;
	private String fromId;
	private String toId;
	private String postId;
	private LocalDateTime notificationTime;
	
	public String getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
		this.toId = toId;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public LocalDateTime getNotificationTime() {
		return notificationTime;
	}
	public void setNotificationTime(LocalDateTime notificationTime) {
		this.notificationTime = notificationTime;
	}

}
