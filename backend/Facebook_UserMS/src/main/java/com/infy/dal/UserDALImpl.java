package com.infy.dal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.infy.model.User;
import com.infy.model.UserFile;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;

@Repository
public class UserDALImpl implements UserDAL {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private GridFsTemplate gridFstemplate;

	@Autowired
	private GridFsOperations operations;

	@Override
	public User findById(String id) {
		return mongoTemplate.findById(new ObjectId(id), User.class);
	}

	@Override
	public User findByEmailId(String emailId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("emailId").is(emailId));
		return mongoTemplate.findOne(query, User.class);
	}

	@Override
	public User saveUser(User user) {
		return mongoTemplate.save(user);
	}

	@Override
	public User findByIdAndUpdate(User user) {
		Query query = new Query(Criteria.where("userId").is(new ObjectId(user.getUserId())));

		DBObject dbDoc = new BasicDBObject();
		mongoTemplate.getConverter().write(user, (Bson) dbDoc);

		Update update = new Update();
		for (String key : dbDoc.keySet()) {
			Object value = dbDoc.get(key);
			if (value != null) {
				update.set(key, value);
			}
		}

		FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);

		return mongoTemplate.findAndModify(query, update, options, User.class);
	}

	@Override
	public List<User> findAllFriends(String id, Integer pageNumber, Integer pageSize) {
		User user = mongoTemplate.findById(new ObjectId(id), User.class);
		Integer skip = pageNumber * pageSize;

		List<User> friendList = new ArrayList<>();

		if (skip >= user.getFriends().size())
			return friendList;

		Integer lastIndex = skip + pageSize - 1;
		if (lastIndex >= user.getFriends().size())
			lastIndex = user.getFriends().size() - 1;

		List<ObjectId> ids = new ArrayList<>();
		for (int i = skip; i <= lastIndex; i++)
			ids.add(user.getFriends().get(i));

		Query query = new Query(Criteria.where("userId").in(ids));

		return mongoTemplate.find(query, User.class);
	}

	@Override
	public void addFriend(String fromId, String toId) {
		User u = mongoTemplate.findById(new ObjectId(fromId), User.class);
		if (u.getFriends() != null && u.getFriends().contains(new ObjectId(toId)))
			return;

		Query query = new Query(Criteria.where("userId").is(new ObjectId(fromId)));
		Update update = new Update();
		update.push("friends", new ObjectId(toId));

		FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);
		mongoTemplate.findAndModify(query, update, options, User.class);	

	}

	@Override
	public ObjectId uploadFile(MultipartFile upload) throws IOException {
		DBObject metadata = new BasicDBObject();
		metadata.put("fileSize", upload.getSize());

		// store in database which returns the objectID
		return gridFstemplate.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(),
				metadata);
	}

	@Override
	public UserFile downloadFile(String id) throws IOException {
		GridFSFile gridFSFile = gridFstemplate.findOne(new Query(Criteria.where("_id").is(id)));

		UserFile loadFile = new UserFile();

		if (gridFSFile != null && gridFSFile.getMetadata() != null) {
			loadFile.setFilename(gridFSFile.getFilename());
			loadFile.setFileType(gridFSFile.getMetadata().get("_contentType").toString());
			loadFile.setFileSize(gridFSFile.getMetadata().get("fileSize").toString());
			loadFile.setFile(IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()));
		}

		return loadFile;
	}

}
