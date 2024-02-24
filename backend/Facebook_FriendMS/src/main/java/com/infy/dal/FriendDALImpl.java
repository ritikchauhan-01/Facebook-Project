package com.infy.dal;

import java.util.ArrayList;
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

import com.infy.model.FriendRequest;
import com.infy.model.Status;

@Repository
public class FriendDALImpl implements FriendDAL {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public FriendRequest findById(String requestId) {
		return mongoTemplate.findById(new ObjectId(requestId), FriendRequest.class);
	}

	@Override
	public List<FriendRequest> findAllByFromId(String id, Integer pageNumber, Integer pageSize) {
		Query query = new Query();
		query.addCriteria(Criteria.where("fromId").is(new ObjectId(id)).and("status").is(Status.PENDING));
		query.skip(pageNumber * pageSize);
		query.limit(pageSize);
		return mongoTemplate.find(query, FriendRequest.class);
	}

	@Override
	public List<FriendRequest> findAllByToId(String id, Integer pageNumber, Integer pageSize) {
		Query query = new Query();
		query.addCriteria(Criteria.where("toId").is(new ObjectId(id)).and("status").is(Status.PENDING));
		query.skip(pageNumber * pageSize);
		query.limit(pageSize);
		return mongoTemplate.find(query, FriendRequest.class);
	}

	@Override
	public FriendRequest createRequest(FriendRequest friendRequest) {
		return mongoTemplate.save(friendRequest);
	}

	@Override
	public FriendRequest findByIdAndUpdateStatus(String requestId, Status s) {
		Query query = new Query(Criteria.where("requestId").is(new ObjectId(requestId)));

		Update update = new Update();
		update.set("status", s);

		FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);

		return mongoTemplate.findAndModify(query, update, options, FriendRequest.class);
	}

	@Override
	public FriendRequest findByFromAndToId(String userId, String toId) {
		List<ObjectId> l = new ArrayList<>();
		l.add(new ObjectId(toId));
		l.add(new ObjectId(userId));
		Query query = new Query();
		query.addCriteria(Criteria.where("toId").in(l).and("fromId").in(l)).with(Sort.by(Sort.Direction.DESC, "requestTime"));
		return mongoTemplate.findOne(query, FriendRequest.class);
	}

}
