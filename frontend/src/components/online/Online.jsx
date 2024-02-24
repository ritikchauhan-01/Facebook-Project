import React from "react";
import "./online.scss";
import { useNavigate } from "react-router-dom";

const Online = ({ user }) => {
  const navigate = useNavigate();

  return (
    <div
      className="profileRightBarFollowing"
      onClick={() => navigate(`/profile/${user.userId}`)}
    >
      <img
        src={
          "http://localhost:6060/facebook/user-api/user/avatar/" + user.userId
        }
        alt="User_Image"
        className="profileRightBarFollowingImg"
        onError={({ currentTarget }) => {
          currentTarget.onerror = null;
          currentTarget.src = "/assets/profileCover/DefaultProfile.jpg";
        }}
      />
      <span className="profileRightBarFollowingName">
        {user.firstName || user.userName}
      </span>
    </div>
  );
};

export default Online;
