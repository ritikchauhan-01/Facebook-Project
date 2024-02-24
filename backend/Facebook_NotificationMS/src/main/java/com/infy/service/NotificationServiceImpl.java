package com.infy.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.infy.dal.NotificationDAL;
import com.infy.dto.FriendDTO;
import com.infy.dto.NotificationDTO;
import com.infy.exception.FacebookException;
import com.infy.model.Event;
import com.infy.model.Notification;

@Service(value = "notificationService")
@Transactional
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private NotificationDAL notificationDAL;

	@Override
	public NotificationDTO getNotificationById(String userId, String notificationId) throws FacebookException {
		Notification userNotification = notificationDAL.getNotificationById(userId);
		if (userNotification == null)
			throw new FacebookException("NotificationService.NO_USER_NOTIFICATION");

		if (!userNotification.getNotifications().contains(new ObjectId(notificationId)))
			throw new FacebookException("NotificationService.NO_NOTIFICATION_FOR_USER");

		Event event = notificationDAL.getEventById(notificationId);
		if (event == null)
			throw new FacebookException("NotificationService.INVALID_NOTIFICATIONID");

		return Event.toDTO(event);
	}

	@Override
	public List<NotificationDTO> getAllNotifications(String userId, Integer pageNumber, Integer pageSize)
			throws FacebookException {
		Notification userNotification = notificationDAL.getNotificationById(userId);
		if (userNotification == null)
			throw new FacebookException("NotificationService.NO_USER_NOTIFICATION");

		List<NotificationDTO> dtos = new ArrayList<>();
		List<Event> events = notificationDAL.getAllEvents(userId, pageNumber, pageSize);

		for (Event e : events)
			dtos.add(Event.toDTO(e));

		return dtos;
	}

	@Override
	public void handleFriendEvent(Event event) {
		Event eventFromDB = notificationDAL.saveEvent(event);

		switch (eventFromDB.getType()) {
		case FR_CREATED: {
			notificationDAL.addNotification(eventFromDB.getToId(), eventFromDB.getEventId());
			template.convertAndSendToUser(eventFromDB.getToId(), "/friend-request", Event.toDTO(eventFromDB));
			break;
		}
		case FR_WITHDRAW: {
			notificationDAL.addNotification(eventFromDB.getToId(), eventFromDB.getEventId());
			template.convertAndSendToUser(eventFromDB.getToId(), "/notify", Event.toDTO(eventFromDB));
			break;
		}
		case FR_ACCEPTED, FR_CANCEL: {
			notificationDAL.addNotification(eventFromDB.getFromId(), eventFromDB.getEventId());
			template.convertAndSendToUser(eventFromDB.getFromId(), "/notify", Event.toDTO(eventFromDB));
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + eventFromDB.getType());
		}
	}

	@Override
	public void handlePostEvent(Event event) {
		Event eventFromDB = notificationDAL.saveEvent(event);
		NotificationDTO notificationDTO = Event.toDTO(eventFromDB);

		for (Integer i = 0;; i++) {
			List<FriendDTO> friendDTOs = getFriends(eventFromDB.getFromId(), i, 50);
			if (friendDTOs.isEmpty())
				break;

			for (FriendDTO friend : friendDTOs) {
				notificationDAL.addNotification(friend.getUserId(), eventFromDB.getEventId());
				template.convertAndSendToUser(friend.getUserId(), "/notify", notificationDTO);
			}
		}
	}

	@Override
	public void handleUserEvent(Event event) {
		Event eventFromDB = notificationDAL.saveEvent(event);
		NotificationDTO notificationDTO = Event.toDTO(eventFromDB);

		for (Integer i = 0;; i++) {
			List<FriendDTO> friendDTOs = getFriends(eventFromDB.getFromId(), i, 50);
			if (friendDTOs.isEmpty())
				break;

			for (FriendDTO friend : friendDTOs) {
				notificationDAL.addNotification(friend.getUserId(), eventFromDB.getEventId());
				template.convertAndSendToUser(friend.getUserId(), "/notify", notificationDTO);
			}
		}
	}

	public List<FriendDTO> getFriends(String userId, Integer pageNumber, Integer pageSize) {
		String userUrl = "http://Facebook-UserMS/facebook/user-api/user/friends/" + userId + "/" + pageNumber + "/"
				+ pageSize;
		ResponseEntity<FriendDTO[]> response = restTemplate.getForEntity(userUrl, FriendDTO[].class);
		FriendDTO[] dtos = response.getBody();

		return Arrays.asList(dtos);
	}

}
