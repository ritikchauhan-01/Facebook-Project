package com.infy.service;

import java.util.List;

import com.infy.dto.FriendRequestDTO;
import com.infy.exception.FacebookException;

public interface FriendService {
	List<FriendRequestDTO> getAllSentRequests(String userId, Integer pageNumber, Integer pageSize)
			throws FacebookException;

	List<FriendRequestDTO> getAllRecievedRequests(String userId, Integer pageNumber, Integer pageSize)
			throws FacebookException;
	
	FriendRequestDTO getStatus(String userId, String toId) throws FacebookException;

	FriendRequestDTO createRequest(FriendRequestDTO friendRequestDTO) throws FacebookException;

	FriendRequestDTO acceptRequest(String userId, String requestId) throws FacebookException;

	FriendRequestDTO cancelRequest(String userId, String requestId) throws FacebookException;

	FriendRequestDTO withdrawRequest(String userId, String requestId) throws FacebookException;
}
