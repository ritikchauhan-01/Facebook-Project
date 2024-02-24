package com.infy.service;

import com.infy.dal.UserDAL;
import com.infy.datacache.CacheStore;
import com.infy.dto.AuthResponse;
import com.infy.dto.FriendDTO;
import com.infy.dto.UserDTO;
import com.infy.exception.FacebookException;
import com.infy.model.*;
import com.infy.repository.ElasticRepository;
import com.infy.utility.HashingUtility;
import com.infy.utility.JwtUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service(value = "userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    CacheStore<UserDTO> userCache;

    @Autowired
    private UserDAL userDAL;

    @Autowired
    private JwtUtil jwt;

    @Autowired
    private KafkaTemplate<String, Event> kafkaTemplate;

    @Autowired
    private ElasticRepository elasticRepository;

    @Value(value = "${message.topic.user.name}")
    private String topicName;

    @Override
    public UserDTO getUserById(String userId) throws FacebookException {
        UserDTO cachedDto = userCache.get(userId);
        if (cachedDto != null)
            return cachedDto;

        User user = userDAL.findById(userId);
        if (user == null)
            throw new FacebookException("UserService.INVALID_USERID");

        UserDTO uDto = User.toUserDTO(user);
        userCache.add(userId, uDto);

        return uDto;
    }

    @Override
    public UserDTO getUserByEmailId(String emailId) throws FacebookException {
        User user = userDAL.findByEmailId(emailId);
        if (user == null)
            throw new FacebookException("UserService.INVALID_EMAILID");

        return User.toUserDTO(user);
    }

    @Override
    public AuthResponse registerUser(UserDTO userDTO) throws FacebookException, NoSuchAlgorithmException {
        // Check if User Email is already in use
        User prevUser = userDAL.findByEmailId(userDTO.getEmailId());
        if (prevUser != null) {
            throw new FacebookException("UserService.USER_ALREADY_EXISTS");
        }

        User tempUser = UserDTO.toUser(userDTO);
        tempUser.setFriends(new ArrayList<>());
        User user = userDAL.saveUser(tempUser);
        elasticRepository.save(User.toElasticUser(user));

        userCache.add(user.getUserId(), User.toUserDTO(user));

        return new AuthResponse(jwt.generate(user, "ACCESS"), jwt.generate(user, "REFRESH"));
    }

    @Override
    public AuthResponse authenticateUser(UserDTO userDTO) throws FacebookException, NoSuchAlgorithmException {
        User user = userDAL.findByEmailId(userDTO.getEmailId());

        if (user == null)
            throw new FacebookException("UserService.INVALID_CREDENTIALS");

        // Match the passwords
        String passwordFromDB = user.getPassword();
        if (passwordFromDB != null) {
            String hashedPassword = HashingUtility.getHashValue(userDTO.getPassword());

            if (hashedPassword.equals(passwordFromDB)) {
                userCache.add(user.getUserId(), User.toUserDTO(user));
                return new AuthResponse(jwt.generate(user, "ACCESS"), jwt.generate(user, "REFRESH"));
            } else {
                throw new FacebookException("UserService.INVALID_CREDENTIALS");
            }
        } else {
            throw new FacebookException("UserService.INVALID_CREDENTIALS");
        }
    }

    @Override
    public UserDTO updateUserDetails(UserDTO userDTO) throws FacebookException, NoSuchAlgorithmException {
        if (userDTO.getEmailId() != null)
            throw new FacebookException("UserService.EMAILID_PRESENT");

        if (userDTO.getPassword() != null)
            throw new FacebookException("UserService.PASSWORD_PRESENT");

        User user = userDAL.findById(userDTO.getUserId());
        if (user == null)
            throw new FacebookException("UserService.INVALID_USERID");

        User tempUser = UserDTO.toUser(userDTO);
        tempUser.setUserId(user.getUserId());

        if (User.checkEqual(user, tempUser))
            return User.toUserDTO(user);

        User updatedUser = userDAL.findByIdAndUpdate(tempUser);
        elasticRepository.save(User.toElasticUser(updatedUser));

        UserDTO uDto = User.toUserDTO(updatedUser);
        userCache.add(user.getUserId(), uDto);

        kafkaTemplate.send(topicName, new Event(user.getUserId(), Type.U_UPDATED_PROFILE));

        return uDto;
    }

    @Override
    public List<FriendDTO> getAllFriends(String userId, Integer pageNumber, Integer pageSize) throws FacebookException {
        User user = userDAL.findById(userId);
        if (user == null)
            throw new FacebookException("UserService.INVALID_USERID");

        List<User> friends = userDAL.findAllFriends(user.getUserId(), pageNumber, pageSize);
        List<FriendDTO> friendDTOs = new ArrayList<>();

        for (User u : friends) {
            FriendDTO dto = User.toFriendDTO(u);
            friendDTOs.add(dto);
        }
        return friendDTOs;
    }

    @Override
    public String addFriend(String fromId, String toId) throws FacebookException {
        userDAL.addFriend(fromId, toId);
        userDAL.addFriend(toId, fromId);

        return "Friend Added";
    }

    @Override
    public UserDTO uploadAvatar(String userId, MultipartFile upload) throws FacebookException, IOException {
        User user = userDAL.findById(userId);
        if (user == null)
            throw new FacebookException("UserService.INVALID_USERID");

        ObjectId avatarID = userDAL.uploadFile(upload);

        User tempUser = new User();
        tempUser.setUserId(user.getUserId());
        tempUser.setAvatarId(avatarID.toString());
        User updatedUser = userDAL.findByIdAndUpdate(tempUser);

        UserDTO uDto = User.toUserDTO(updatedUser);
        userCache.add(user.getUserId(), uDto);

        kafkaTemplate.send(topicName, new Event(user.getUserId(), Type.U_UPDATED_AVATAR));

        return uDto;
    }

    @Override
    public UserFile downloadAvatar(String userId) throws FacebookException, IOException {
        UserDTO dto = userCache.get(userId);
        if (dto == null) {
            User user = userDAL.findById(userId);
            if (user == null)
                throw new FacebookException("UserService.INVALID_USERID");
            dto = User.toUserDTO(user);
        }

        if (dto.getAvatarId() == null || dto.getAvatarId().isBlank())
            throw new FacebookException("UserService.INVALID_AVATARID");

        return userDAL.downloadFile(dto.getAvatarId());
    }

    @Override
    public List<UserDTO> searchUsers(String keyword) throws FacebookException {
        List<ElasticUser> eUsers = elasticRepository.searchUsers(keyword);
        List<UserDTO> dtos = new ArrayList<>();

        for (ElasticUser eu : eUsers)
            dtos.add(ElasticUser.toUserDTO(eu));

        return dtos;
    }

}
