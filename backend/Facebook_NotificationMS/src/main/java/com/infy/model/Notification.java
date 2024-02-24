package com.infy.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notification")
public class Notification {

	@Id
	private String userId;
	private List<ObjectId> notifications;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<ObjectId> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<ObjectId> notifications) {
		this.notifications = notifications;
	}
}
