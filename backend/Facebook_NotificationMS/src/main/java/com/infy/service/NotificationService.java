package com.infy.service;

import java.util.List;

import com.infy.dto.NotificationDTO;
import com.infy.exception.FacebookException;
import com.infy.model.Event;

public interface NotificationService {
	NotificationDTO getNotificationById(String userId, String notificationId) throws FacebookException;

	List<NotificationDTO> getAllNotifications(String userId, Integer pageNumber, Integer pageSize)
			throws FacebookException;
	
	void handleFriendEvent(Event event);
	
	void handlePostEvent(Event event);
	
	void handleUserEvent(Event event);
}
