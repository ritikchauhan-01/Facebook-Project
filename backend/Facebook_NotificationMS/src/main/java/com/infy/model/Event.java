package com.infy.model;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.infy.dto.NotificationDTO;

@Document(collection = "event")
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

	public static NotificationDTO toDTO(Event e) {
		NotificationDTO dto = new NotificationDTO();

		dto.setNotificationId(e.getEventId());
		dto.setFromId(e.getFromId());
		dto.setToId(e.getToId());
		dto.setPostId(e.getPostId());
		dto.setNotificationTime(e.getEventTime());
		dto.setType(e.getType().toString());

		switch (e.getType()) {
		case FR_CREATED: {
			dto.setDescription("Sent Friend Request");
			break;
		}
		case FR_ACCEPTED: {
			dto.setDescription("Accepted Friend Request");
			break;
		}
		case FR_CANCEL: {
			dto.setDescription("Canceled Friend Request");
			break;
		}
		case FR_WITHDRAW: {
			dto.setDescription("Withdrawn Friend Request");
			break;
		}
		case P_CREATED: {
			dto.setDescription("Created New Post");
			break;
		}
		case U_UPDATED_AVATAR: {
			dto.setDescription("Updated Avatar Pic");
			break;
		}
		case U_UPDATED_PROFILE: {
			dto.setDescription("Updated Personal Information");
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + e.getType());
		}

		return dto;
	}
}
