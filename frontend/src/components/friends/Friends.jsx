import React from "react";
import "./friends.scss";
import { useNavigate } from "react-router-dom";

const Friends = ({ user }) => {
  const navigate = useNavigate();
  return (
    <div>
      <li
        className="sidebarFriend"
        onClick={() => navigate(`/profile/${user.userId}`)}
      >
        <img
          src={
            "http://localhost:6060/facebook/user-api/user/avatar/" + user.userId
          }
          alt="User_Image"
          className="sidebarFriendImg"
          onError={({ currentTarget }) => {
            currentTarget.onerror = null;
            currentTarget.src = "/assets/profileCover/DefaultProfile.jpg";
          }}
        />
        <span className="sidebarFriendName">
          {user.firstName || user.userName}
        </span>
      </li>
    </div>
  );
};

export default Friends;
