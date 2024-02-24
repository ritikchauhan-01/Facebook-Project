package com.infy.dal;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.infy.model.Post;
import com.infy.model.UserFile;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;

@Repository
public class PostDALImpl implements PostDAL {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private GridFsTemplate gridFstemplate;

	@Autowired
	private GridFsOperations operations;

	@Override
	public Post findById(String id) {
		return mongoTemplate.findById(new ObjectId(id), Post.class);
	}

	@Override
	public List<Post> findAllByUserId(String id, Integer pageNumber, Integer pageSize) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(new ObjectId(id))).with(Sort.by(Sort.Direction.DESC, "postTime"));
		query.skip(pageNumber * pageSize);
		query.limit(pageSize);
		return mongoTemplate.find(query, Post.class);
	}

	@Override
	public Post savePost(Post post) {
		return mongoTemplate.save(post);
	}

	@Override
	public Post findByIdAndUpdate(Post post) {
		Query query = new Query(Criteria.where("postId").is(new ObjectId(post.getPostId())));

		DBObject dbDoc = new BasicDBObject();
		mongoTemplate.getConverter().write(post, (Bson) dbDoc);

		Update update = new Update();
		for (String key : dbDoc.keySet()) {
			Object value = dbDoc.get(key);
			if (value != null) {
				update.set(key, value);
			}
		}

		FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);

		return mongoTemplate.findAndModify(query, update, options, Post.class);
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

	@Override
	public List<Post> findByIds(List<ObjectId> ids) {
		Query query = new Query(Criteria.where("postId").in(ids));
		return mongoTemplate.find(query, Post.class);
	}

}
