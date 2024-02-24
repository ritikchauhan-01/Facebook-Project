package com.infy.api;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;

import com.infy.dto.FriendDTO;
import com.infy.dto.FriendRequestDTO;
import com.infy.exception.FacebookException;
import com.infy.service.FriendService;

@RestController
@RequestMapping(value = "friend-api")
@Validated
@CrossOrigin
public class FriendAPI {
	@Autowired
	private FriendService friendService;

	public static final Log LOGGER = LogFactory.getLog(FriendAPI.class);

	@GetMapping("/sent/{pageNumber}/{pageSize}")
	public ResponseEntity<List<FriendRequestDTO>> getAllSentRequests(@RequestHeader("userId") String userId,
			@PathVariable Integer pageNumber, @PathVariable Integer pageSize) throws FacebookException {
		return new ResponseEntity<>(friendService.getAllSentRequests(userId, pageNumber, pageSize), HttpStatus.OK);
	}

	@PostMapping("/request")
	public ResponseEntity<FriendRequestDTO> createRequest(@RequestHeader("userId") String userId,
			@RequestBody FriendRequestDTO friendRequestDTO) throws FacebookException {
		FriendDTO fDto = new FriendDTO();
		fDto.setUserId(userId);
		friendRequestDTO.setFrom(fDto);
		return new ResponseEntity<>(friendService.createRequest(friendRequestDTO), HttpStatus.CREATED);
	}

	@GetMapping("/received/{pageNumber}/{pageSize}")
	public ResponseEntity<List<FriendRequestDTO>> getAllRecievedRequests(@RequestHeader("userId") String userId,
			@PathVariable Integer pageNumber, @PathVariable Integer pageSize) throws FacebookException {
		return new ResponseEntity<>(friendService.getAllRecievedRequests(userId, pageNumber, pageSize), HttpStatus.OK);
	}

	@PutMapping("/accept/{requestId}")
	public ResponseEntity<FriendRequestDTO> acceptRequest(@RequestHeader("userId") String userId, @PathVariable String requestId) throws FacebookException {
		return new ResponseEntity<>(friendService.acceptRequest(userId, requestId), HttpStatus.OK);
	}

	@PutMapping("/cancel/{requestId}")
	public ResponseEntity<FriendRequestDTO> cancelRequest(@RequestHeader("userId") String userId, @PathVariable String requestId) throws FacebookException {
		return new ResponseEntity<>(friendService.cancelRequest(userId, requestId), HttpStatus.OK);
	}

	@PutMapping("/withdraw/{requestId}")
	public ResponseEntity<FriendRequestDTO> withdrawRequest(@RequestHeader("userId") String userId, @PathVariable String requestId) throws FacebookException {
		return new ResponseEntity<>(friendService.withdrawRequest(userId, requestId), HttpStatus.OK);
	}
	
	@GetMapping("/status/{toId}")
	public ResponseEntity<FriendRequestDTO> getStatus(@RequestHeader("userId") String userId,
			@PathVariable String toId) throws FacebookException {
		return new ResponseEntity<>(friendService.getStatus(userId, toId), HttpStatus.OK);
	}
}
