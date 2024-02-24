package com.infy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.infy.dto.UserDTO;

@Document(indexName = "user")
public class ElasticUser {

	@Id
	private String userId;

	@Field(type = FieldType.Keyword)
	private String firstName;

	@Field(type = FieldType.Keyword)
	private String lastName;

	@Field(type = FieldType.Keyword)
	private String fullName;

	@Field(type = FieldType.Keyword)
	private String userName;

	@Field(type = FieldType.Keyword)
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public static UserDTO toUserDTO(ElasticUser eu) {
		UserDTO dto = new UserDTO();

		dto.setUserId(eu.getUserId());
		dto.setEmailId(eu.getEmailId());
		dto.setFirstName(eu.getFirstName());
		dto.setLastName(eu.getLastName());
		dto.setUserName(eu.getUserName());

		return dto;
	}
}
