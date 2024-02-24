package com.infy.model;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.infy.dto.FriendDTO;
import com.infy.dto.FriendRequestDTO;

@Document(collection = "friendRequest")
public class FriendRequest {

	@Id
	private String requestId;
	private ObjectId fromId;
	private ObjectId toId;
	private Status status;

	@CreatedDate
	private LocalDateTime requestTime;

	public LocalDateTime getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(LocalDateTime requestTime) {
		this.requestTime = requestTime;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public static FriendRequestDTO toFriendRequestDTO(FriendRequest r) {
		FriendRequestDTO dto = new FriendRequestDTO();

		dto.setRequestId(r.getRequestId());
		dto.setRequestTime(r.getRequestTime());
		dto.setStatus(r.getStatus().toString());

		FriendDTO fromDto = new FriendDTO();
		fromDto.setUserId(r.getFromId());

		FriendDTO toDto = new FriendDTO();
		toDto.setUserId(r.getToId());

		dto.setFrom(fromDto);
		dto.setTo(toDto);

		return dto;
	}

	public static Event toEvent(FriendRequest r) {
		Event event = new Event();

		event.setFromId(r.getFromId());
		event.setToId(r.getToId());
		event.setEventTime(LocalDateTime.now());

		switch (r.getStatus()) {
		case PENDING: {
			event.setType(Type.FR_CREATED);
			break;
		}
		case ACCEPTED: {
			event.setType(Type.FR_ACCEPTED);
			break;
		}
		case CANCEL: {
			event.setType(Type.FR_CANCEL);
			break;
		}
		case WITHDRAW: {
			event.setType(Type.FR_WITHDRAW);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + r.getStatus());
		}

		return event;
	}

}
