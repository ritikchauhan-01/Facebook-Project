package com.infy.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.infy.model.Event;
import com.infy.service.NotificationService;

@Component
public class KafkaConsumer {

	@Autowired
	private NotificationService notificationService;

	@KafkaListener(topics = "${message.topic.friend.name}", containerFactory = "kafkaListenerContainerFactory")
	public void listenFromFriendMS(Event event) {
		notificationService.handleFriendEvent(event);
	}

	@KafkaListener(topics = "${message.topic.post.name}", containerFactory = "kafkaListenerContainerFactory")
	public void listenFromPostMS(Event event) {
		notificationService.handlePostEvent(event);
	}

	@KafkaListener(topics = "${message.topic.user.name}", containerFactory = "kafkaListenerContainerFactory")
	public void listenFromUserMS(Event event) {
		notificationService.handleUserEvent(event);
	}

}
