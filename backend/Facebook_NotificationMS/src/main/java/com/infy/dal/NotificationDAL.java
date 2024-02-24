package com.infy.dal;

import java.util.List;

import com.infy.model.Event;
import com.infy.model.Notification;

public interface NotificationDAL {
	Event saveEvent(Event event);

	Event getEventById(String eventId);

	List<Event> getAllEvents(String userId, Integer pageNumber, Integer pageSize);

	Notification getNotificationById(String userId);

	Void addNotification(String userId, String eventId);
}
