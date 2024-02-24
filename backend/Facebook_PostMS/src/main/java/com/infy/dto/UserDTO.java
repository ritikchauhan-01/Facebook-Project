package com.infy.dto;

import jakarta.validation.constraints.Email;

public class UserDTO {

	private String userId;

	private String userName;

	@Email(message = "{user.email.invalid}")
	private String emailId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
