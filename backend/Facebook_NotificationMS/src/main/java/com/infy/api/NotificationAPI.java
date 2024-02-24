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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.dto.NotificationDTO;
import com.infy.exception.FacebookException;
import com.infy.service.NotificationService;

@RestController
@RequestMapping(value = "notification-api")
@Validated
@CrossOrigin
public class NotificationAPI {

	@Autowired
	private NotificationService notificationService;

	public static final Log LOGGER = LogFactory.getLog(NotificationAPI.class);

	@GetMapping(value = "/notification/{notificationId}")
	public ResponseEntity<NotificationDTO> getPostById(@RequestHeader("userId") String userId,
			@PathVariable String notificationId) throws FacebookException {
		return new ResponseEntity<>(notificationService.getNotificationById(userId, notificationId), HttpStatus.OK);
	}

	@GetMapping(value = "/notifications/{pageNumber}/{pageSize}")
	public ResponseEntity<List<NotificationDTO>> getAllPosts(@RequestHeader("userId") String userId,
			@PathVariable Integer pageNumber, @PathVariable Integer pageSize) throws FacebookException {
		return new ResponseEntity<>(notificationService.getAllNotifications(userId, pageNumber, pageSize),
				HttpStatus.OK);
	}
}
