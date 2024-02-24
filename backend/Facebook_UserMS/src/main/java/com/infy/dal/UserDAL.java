package com.infy.dal;

import java.io.IOException;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import com.infy.model.User;
import com.infy.model.UserFile;

public interface UserDAL {
	User findById(String id);

	User findByEmailId(String emailId);

	User saveUser(User user);

	User findByIdAndUpdate(User user);

	List<User> findAllFriends(String id, Integer pageNumber, Integer pageSize);

	void addFriend(String fromId, String toId);

	ObjectId uploadFile(MultipartFile upload) throws IOException;

	UserFile downloadFile(String id) throws IOException;
}
