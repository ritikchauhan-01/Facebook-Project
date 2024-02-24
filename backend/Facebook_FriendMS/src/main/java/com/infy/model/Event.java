package com.infy.model;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

public class Event {

	@Id
	private String eventId;
	private ObjectId fromId;
	private ObjectId toId;
	private ObjectId postId;
	private Type type;

	@CreatedDate
	private LocalDateTime eventTime;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getFromId() {
		if (fromId == null)
			return null;
		return fromId.toString();
	}

	public void setFromId(String fromId) {
		if (fromId != null)
			this.fromId = new ObjectId(fromId);
	}

	public String getToId() {
		if (toId == null)
			return null;
		return toId.toString();
	}

	public void setToId(String toId) {
		if (toId != null)
			this.toId = new ObjectId(toId);
	}

	public String getPostId() {
		if (postId == null)
			return null;
		return postId.toString();
	}

	public void setPostId(String postId) {
		if (postId != null)
			this.postId = new ObjectId(postId);
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public LocalDateTime getEventTime() {
		return eventTime;
	}

	public void setEventTime(LocalDateTime eventTime) {
		this.eventTime = eventTime;
	}
}
