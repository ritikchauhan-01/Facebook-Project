package com.infy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class FriendDTO {
	private String userId;

	private String firstName;

	private String lastName;

	@NotNull(message = "{user.userName.notPresent}")
	private String userName;

	@NotNull(message = "{user.email.notpresent}")
	@Email(message = "{user.email.invalid}")
	private String emailId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
}
