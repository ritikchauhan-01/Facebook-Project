package com.infy.dal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.infy.model.Event;
import com.infy.model.Notification;

@Repository
public class NotificationDALImpl implements NotificationDAL {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Event saveEvent(Event event) {
		return mongoTemplate.save(event);
	}

	@Override
	public Event getEventById(String eventId) {
		return mongoTemplate.findById(new ObjectId(eventId), Event.class);
	}

	@Override
	public List<Event> getAllEvents(String userId, Integer pageNumber, Integer pageSize) {
		Notification userNotification = mongoTemplate.findById(new ObjectId(userId), Notification.class);
		Integer skip = pageNumber * pageSize;

		List<ObjectId> temp = userNotification.getNotifications();
		Collections.reverse(temp);

		List<Event> events = new ArrayList<>();

		if (skip >= userNotification.getNotifications().size())
			return events;

		Integer lastIndex = skip + pageSize - 1;
		if (lastIndex >= temp.size())
			lastIndex = temp.size() - 1;

		List<ObjectId> ids = new ArrayList<>();
		for (int i = skip; i <= lastIndex; i++)
			ids.add(temp.get(i));

		Query query = new Query();
		query.addCriteria(Criteria.where("eventId").in(ids)).with(Sort.by(Sort.Direction.DESC, "eventTime"));

		return mongoTemplate.find(query, Event.class);
	}

	@Override
	public Notification getNotificationById(String userId) {
		return mongoTemplate.findById(new ObjectId(userId), Notification.class);
	}

	@Override
	public Void addNotification(String userId, String eventId) {
		Notification user = mongoTemplate.findById(new ObjectId(userId), Notification.class);

		if (user == null) {
			// create user notification and add notification
			Notification u = new Notification();
			u.setUserId(userId);
			List<ObjectId> ns = new ArrayList<>();
			ns.add(new ObjectId(eventId));
			u.setNotifications(ns);
			mongoTemplate.save(u);
			return null;
		}

		Query query = new Query(Criteria.where("userId").is(new ObjectId(userId)));
		Update update = new Update();
		update.push("notifications", new ObjectId(eventId));

		FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);
		mongoTemplate.findAndModify(query, update, options, Notification.class);
		return null;
	}

}
