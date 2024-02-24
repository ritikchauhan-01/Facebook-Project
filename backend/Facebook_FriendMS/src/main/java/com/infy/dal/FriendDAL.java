package com.infy.dal;

import java.util.List;

import com.infy.model.FriendRequest;
import com.infy.model.Status;

public interface FriendDAL {
	FriendRequest findById(String requestId);

	List<FriendRequest> findAllByFromId(String id, Integer pageNumber, Integer pageSize);

	List<FriendRequest> findAllByToId(String id, Integer pageNumber, Integer pageSize);
	
	FriendRequest findByFromAndToId(String userId, String toId);

	FriendRequest createRequest(FriendRequest friendRequest);

	FriendRequest findByIdAndUpdateStatus(String requestId, Status s);
}
