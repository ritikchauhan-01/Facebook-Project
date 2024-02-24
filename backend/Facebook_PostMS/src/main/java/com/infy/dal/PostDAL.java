package com.infy.dal;

import java.io.IOException;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import com.infy.model.Post;
import com.infy.model.UserFile;

public interface PostDAL {
	Post findById(String id);

	List<Post> findAllByUserId(String id, Integer pageNumber, Integer pageSize);
	
	List<Post> findByIds(List<ObjectId> ids);

	Post findByIdAndUpdate(Post post);

	Post savePost(Post post);

	ObjectId uploadFile(MultipartFile upload) throws IOException;

	UserFile downloadFile(String id) throws IOException;
}
