package com.infy.api;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.infy.dto.AuthResponse;
import com.infy.dto.FriendDTO;
import com.infy.dto.UserDTO;
import com.infy.exception.FacebookException;
import com.infy.model.UserFile;
import com.infy.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "user-api")
@Validated
@CrossOrigin
public class UserAPI {
	@Autowired
	private UserService userService;

	public static final Log LOGGER = LogFactory.getLog(UserAPI.class);

	@GetMapping(value = "/user/{userId}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable String userId) throws FacebookException {
		UserDTO user = userService.getUserById(userId);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping(value = "/user/me")
	public ResponseEntity<UserDTO> getMe(@RequestHeader("userId") String userId) throws FacebookException {

		UserDTO user = userService.getUserById(userId);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping(value = "/userEmail/{userEmailId}")
	public ResponseEntity<UserDTO> getUserByEmailId(@PathVariable String userEmailId) throws FacebookException {
		UserDTO user = userService.getUserByEmailId(userEmailId);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PostMapping("/user/register")
	public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody UserDTO userDTO) throws Exception {
		return new ResponseEntity<>(userService.registerUser(userDTO), HttpStatus.CREATED);
	}

	@PostMapping(value = "/user/login")
	public ResponseEntity<AuthResponse> authenticateUser(@RequestBody UserDTO userDTO) throws Exception {
		return new ResponseEntity<>(userService.authenticateUser(userDTO), HttpStatus.OK);
	}

	@GetMapping("/user/friends/{userId}/{pageNumber}/{pageSize}")
	public ResponseEntity<List<FriendDTO>> getAllFriends(@PathVariable String userId, @PathVariable Integer pageNumber,
			@PathVariable Integer pageSize) throws FacebookException {
		return new ResponseEntity<>(userService.getAllFriends(userId, pageNumber, pageSize), HttpStatus.OK);
	}

	@PutMapping("/user/add-friend/{fromId}/{toId}")
	public ResponseEntity<String> addFriend(@PathVariable String fromId, @PathVariable String toId)
			throws FacebookException {
		return new ResponseEntity<>(userService.addFriend(fromId, toId), HttpStatus.OK);
	}

	@PutMapping("/user/update-profile")
	public ResponseEntity<UserDTO> updateUser(@RequestHeader("userId") String userId, @RequestBody UserDTO userDTO)
			throws Exception {
		userDTO.setUserId(userId);
		UserDTO dto = userService.updateUserDetails(userDTO);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@PostMapping("/user/avatar")
	public ResponseEntity<UserDTO> upload(@RequestHeader("userId") String userId,
			@RequestParam("avatar") MultipartFile avatar) throws Exception {
		return new ResponseEntity<>(userService.uploadAvatar(userId, avatar), HttpStatus.OK);
	}

	@GetMapping("/user/avatar/{userId}")
	public ResponseEntity<ByteArrayResource> download(@PathVariable String userId) throws Exception {
		UserFile avatar = userService.downloadAvatar(userId);

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(avatar.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + avatar.getFilename() + "\"")
				.body(new ByteArrayResource(avatar.getFile()));
	}

	@GetMapping(value = "/user/search")
	public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String keyword) throws FacebookException {
		return new ResponseEntity<>(userService.searchUsers(keyword), HttpStatus.OK);
	}
}
