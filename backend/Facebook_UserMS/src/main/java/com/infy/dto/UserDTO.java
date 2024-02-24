package com.infy.dto;

import java.security.NoSuchAlgorithmException;

import com.infy.model.User;
import com.infy.utility.HashingUtility;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;

public class UserDTO {

	private String userId;

	private String firstName;

	private String lastName;

	@NotNull(message = "{user.userName.notPresent}")
	private String userName;

	@NotNull(message = "{user.email.notpresent}")
	@Email(message = "{user.email.invalid}")
	private String emailId;

	@Pattern(regexp = "[6-9]\\d{9}", message = "{user.contact.invalid}")
	private String contactNumber;

	@NotNull(message = "{user.password.notpresent}")
	@Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{6,20})", message = "{user.password.invalid}")
	private String password;

	private String address;

	private String country;

	private String avatarId;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAvatarId() {
		return avatarId;
	}

	public void setAvatarId(String avatarId) {
		this.avatarId = avatarId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public static User toUser(UserDTO uDTO) throws NoSuchAlgorithmException {
		User user = new User();

		user.setUserName(uDTO.getUserName());
		user.setFirstName(uDTO.getFirstName());
		user.setLastName(uDTO.getLastName());
		user.setEmailId(uDTO.getEmailId());
		user.setContactNumber(uDTO.getContactNumber());
		user.setAddress(uDTO.getAddress());
		user.setCountry(uDTO.getCountry());
		user.setAvatarId(uDTO.getAvatarId());

		if (uDTO.getPassword() != null && !uDTO.getPassword().isBlank())
			user.setPassword(HashingUtility.getHashValue(uDTO.getPassword()));

		return user;
	}
}
