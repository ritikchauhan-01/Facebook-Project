package com.infy.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.infy.dal.FriendDAL;
import com.infy.dto.FriendDTO;
import com.infy.dto.FriendRequestDTO;
import com.infy.exception.FacebookException;
import com.infy.model.Event;
import com.infy.model.FriendRequest;
import com.infy.model.Status;

@Service(value = "friendService")
@Transactional
public class FriendServiceImpl implements FriendService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private FriendDAL friendDAL;

	@Autowired
	private KafkaTemplate<String, Event> kafkaTemplate;

	@Value(value = "${message.topic.friend.name}")
	private String topicName;

	private static final String USER_URL = "http://Facebook-UserMS/facebook/user-api/user/";

	@Override
	public List<FriendRequestDTO> getAllSentRequests(String userId, Integer pageNumber, Integer pageSize)
			throws FacebookException {
		FriendDTO fromDTO = restTemplate.getForObject(USER_URL + userId, FriendDTO.class);

		List<FriendRequest> friendRequests = friendDAL.findAllByFromId(fromDTO.getUserId(), pageNumber, pageSize);
		List<FriendRequestDTO> requestDTOs = new ArrayList<>();

		for (FriendRequest r : friendRequests) {
			FriendRequestDTO dto = FriendRequest.toFriendRequestDTO(r);
			dto.setFrom(fromDTO);
			dto.setTo(restTemplate.getForObject(USER_URL + dto.getTo().getUserId(), FriendDTO.class));
			requestDTOs.add(dto);
		}
		return requestDTOs;
	}

	@Override
	public List<FriendRequestDTO> getAllRecievedRequests(String userId, Integer pageNumber, Integer pageSize)
			throws FacebookException {
		FriendDTO toDTO = restTemplate.getForObject(USER_URL + userId, FriendDTO.class);

		List<FriendRequest> friendRequests = friendDAL.findAllByToId(toDTO.getUserId(), pageNumber, pageSize);
		List<FriendRequestDTO> requestDTOs = new ArrayList<>();

		for (FriendRequest r : friendRequests) {
			FriendRequestDTO dto = FriendRequest.toFriendRequestDTO(r);
			dto.setFrom(restTemplate.getForObject(USER_URL + dto.getFrom().getUserId(), FriendDTO.class));
			dto.setTo(toDTO);
			requestDTOs.add(dto);
		}
		return requestDTOs;
	}

	@Override
	public FriendRequestDTO createRequest(FriendRequestDTO friendRequestDTO) throws FacebookException {
		friendRequestDTO = this.populateUsers(friendRequestDTO);

		FriendRequest friendRequest = friendDAL.createRequest(FriendRequestDTO.toFriendRequest(friendRequestDTO));

		FriendRequestDTO dto = FriendRequest.toFriendRequestDTO(friendRequest);
		dto.setFrom(friendRequestDTO.getFrom());
		dto.setTo(friendRequestDTO.getTo());

		kafkaTemplate.send(topicName, FriendRequest.toEvent(friendRequest));

		return dto;
	}

	@Override
	public FriendRequestDTO acceptRequest(String userId, String requestId) throws FacebookException {
		FriendRequest friendRequest = friendDAL.findById(requestId);
		if (friendRequest == null)
			throw new FacebookException("FriendService.INVALID_REQUESTID");

		if (friendRequest.getStatus() != Status.PENDING)
			throw new FacebookException("FriendService.SETTLED_REQUEST");

		if (!friendRequest.getToId().equals(userId))
			throw new FacebookException("FriendService.USER_NOT_ALLOWED");

		// add friend to friend list
		try {
			restTemplate.put(USER_URL + "add-friend/" + friendRequest.getFromId() + "/" + friendRequest.getToId(),
					String.class);
		} catch (Exception e) {
			throw new FacebookException("FriendService.ERROR");
		}

		FriendRequest fr = friendDAL.findByIdAndUpdateStatus(friendRequest.getRequestId(), Status.ACCEPTED);
		kafkaTemplate.send(topicName, FriendRequest.toEvent(fr));

		return this.populateUsers(FriendRequest.toFriendRequestDTO(fr));
	}

	@Override
	public FriendRequestDTO cancelRequest(String userId, String requestId) throws FacebookException {
		FriendRequest friendRequest = friendDAL.findById(requestId);
		if (friendRequest == null)
			throw new FacebookException("FriendService.INVALID_REQUESTID");

		if (friendRequest.getStatus() != Status.PENDING)
			throw new FacebookException("FriendService.SETTLED_REQUEST");

		if (!friendRequest.getToId().equals(userId))
			throw new FacebookException("FriendService.USER_NOT_ALLOWED");

		FriendRequest fr = friendDAL.findByIdAndUpdateStatus(friendRequest.getRequestId(), Status.CANCEL);
		kafkaTemplate.send(topicName, FriendRequest.toEvent(fr));

		return this.populateUsers(FriendRequest.toFriendRequestDTO(fr));
	}

	@Override
	public FriendRequestDTO withdrawRequest(String userId, String requestId) throws FacebookException {
		FriendRequest friendRequest = friendDAL.findById(requestId);
		if (friendRequest == null)
			throw new FacebookException("FriendService.INVALID_REQUESTID");

		if (friendRequest.getStatus() != Status.PENDING)
			throw new FacebookException("FriendService.SETTLED_REQUEST");

		if (!friendRequest.getFromId().equals(userId))
			throw new FacebookException("FriendService.USER_NOT_ALLOWED");

		FriendRequest fr = friendDAL.findByIdAndUpdateStatus(friendRequest.getRequestId(), Status.WITHDRAW);
		kafkaTemplate.send(topicName, FriendRequest.toEvent(fr));

		return this.populateUsers(FriendRequest.toFriendRequestDTO(fr));
	}

	public FriendRequestDTO populateUsers(FriendRequestDTO dto) throws FacebookException {
		try {
			dto.setFrom(restTemplate.getForObject(USER_URL + dto.getFrom().getUserId(), FriendDTO.class));
		} catch (Exception e) {
			throw new FacebookException("FriendService.INVALID_FORMID");
		}

		try {
			dto.setTo(restTemplate.getForObject(USER_URL + dto.getTo().getUserId(), FriendDTO.class));
		} catch (Exception e) {
			throw new FacebookException("FriendService.INVALID_TOID");
		}

		return dto;
	}

	@Override
	public FriendRequestDTO getStatus(String userId, String toId) throws FacebookException {
		FriendRequest fr = friendDAL.findByFromAndToId(userId, toId);
		if (fr == null)
			return null;
		return FriendRequest.toFriendRequestDTO(fr);
	}

}
