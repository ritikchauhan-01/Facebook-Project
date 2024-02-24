package com.infy.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.infy.dto.AuthResponse;
import com.infy.dto.FriendDTO;
import com.infy.dto.UserDTO;
import com.infy.exception.FacebookException;
import com.infy.model.UserFile;

public interface UserService {
	UserDTO getUserById(String userId) throws FacebookException;

	UserDTO getUserByEmailId(String emailId) throws FacebookException;

	AuthResponse registerUser(UserDTO userDTO) throws FacebookException, NoSuchAlgorithmException;

	AuthResponse authenticateUser(UserDTO userDTO) throws FacebookException, NoSuchAlgorithmException;

	UserDTO updateUserDetails(UserDTO userDTO) throws FacebookException, NoSuchAlgorithmException;

	List<FriendDTO> getAllFriends(String userId, Integer pageNumber, Integer pageSize) throws FacebookException;

	String addFriend(String fromId, String toId) throws FacebookException;

	UserDTO uploadAvatar(String userId, MultipartFile upload) throws FacebookException, IOException;

	UserFile downloadAvatar(String userId) throws FacebookException, IOException;
	
	List<UserDTO> searchUsers(String keyword) throws FacebookException;
}
