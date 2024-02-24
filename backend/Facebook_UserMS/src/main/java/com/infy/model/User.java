package com.infy.model;

import java.util.List;
import java.util.Objects;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.infy.dto.FriendDTO;
import com.infy.dto.UserDTO;

@Document(collection = "user")
public class User {

    @Id
    private String userId;
    private String firstName;
    private String lastName;
    private String userName;
    private String emailId;
    private String contactNumber;
    private String password;
    private String address;
    private String country;
    private ObjectId avatarId;
    private List<ObjectId> friends;

    public List<ObjectId> getFriends() {
        return friends;
    }

    public void setFriends(List<ObjectId> friends) {
        this.friends = friends;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAvatarId() {
        if (avatarId == null)
            return null;
        return avatarId.toString();
    }

    public void setAvatarId(String avatarId) {
        if (avatarId != null)
            this.avatarId = new ObjectId(avatarId);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public static UserDTO toUserDTO(User u) {
        UserDTO dto = new UserDTO();

        dto.setUserName(u.getUserName());
        dto.setEmailId(u.getEmailId());
        dto.setUserId(u.getUserId());
        dto.setContactNumber(u.getContactNumber());
        dto.setFirstName(u.getFirstName());
        dto.setLastName(u.getLastName());
        dto.setAvatarId(u.getAvatarId());
        dto.setAddress(u.getAddress());
        dto.setCountry(u.getCountry());

        return dto;
    }

    public static FriendDTO toFriendDTO(User u) {
        FriendDTO dto = new FriendDTO();

        dto.setUserName(u.getUserName());
        dto.setEmailId(u.getEmailId());
        dto.setUserId(u.getUserId());
        dto.setFirstName(u.getFirstName());
        dto.setLastName(u.getLastName());

        return dto;
    }

    public static ElasticUser toElasticUser(User u) {
        ElasticUser eUser = new ElasticUser();

        eUser.setUserId(u.getUserId());
        eUser.setEmailId(u.getEmailId());
        eUser.setFirstName(u.getFirstName());
        eUser.setLastName(u.getLastName());
        eUser.setUserName(u.getUserName());

        if (u.getFirstName() != null && u.getLastName() != null)
            eUser.setFullName(u.getFirstName() + " " + u.getLastName());
        else if (u.getFirstName() != null && u.getLastName() == null)
            eUser.setFullName(u.getFirstName());
        else if (u.getFirstName() == null && u.getLastName() != null)
            eUser.setFullName(u.getLastName());

        return eUser;
    }

    public static Boolean checkEqual(User u1, User u2) {
        return Objects.equals(u1.getUserId(), u2.getUserId()) &&
                Objects.equals(u1.getUserName(), u2.getUserName()) &&
                Objects.equals(u1.getFirstName(), u2.getFirstName()) &&
                Objects.equals(u1.getLastName(), u2.getLastName()) &&
                Objects.equals(u1.getContactNumber(), u2.getContactNumber()) &&
                Objects.equals(u1.getAddress(), u2.getAddress()) &&
                Objects.equals(u1.getCountry(), u2.getCountry());
    }
}
