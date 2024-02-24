package com.infy.dto;

import java.time.LocalDateTime;

import com.infy.model.FriendRequest;
import com.infy.model.Status;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public class FriendRequestDTO {
	private String requestId;

	@NotNull(message = "{friend.Status.notPresent}")
	private String status;

	@PastOrPresent(message = "{friend.requestTime.invalid}")
	private LocalDateTime requestTime;

	@Valid
	private FriendDTO from;

	@Valid
	private FriendDTO to;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(LocalDateTime requestTime) {
		this.requestTime = requestTime;
	}

	public FriendDTO getFrom() {
		return from;
	}

	public void setFrom(FriendDTO from) {
		this.from = from;
	}

	public FriendDTO getTo() {
		return to;
	}

	public void setTo(FriendDTO to) {
		this.to = to;
	}

	public static FriendRequest toFriendRequest(FriendRequestDTO dto) {
		FriendRequest r = new FriendRequest();

		r.setStatus(Status.PENDING);
		r.setRequestTime(LocalDateTime.now());

		if (dto.getFrom() != null && dto.getFrom().getUserId() != null)
			r.setFromId(dto.getFrom().getUserId());

		if (dto.getTo() != null && dto.getTo().getUserId() != null)
			r.setToId(dto.getTo().getUserId());

		return r;
	}
}
