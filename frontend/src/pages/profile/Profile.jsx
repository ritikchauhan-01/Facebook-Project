import React, { useContext, useEffect } from "react";
import Navbar from "../../components/navbar/Navbar";
import Sidebar from "../../components/sidebar/Sidebar";
import "./profile.scss";
import Feed from "./../../components/feed/Feed";
import Rightbar from "./../../components/rightbar/Rightbar";
import AuthContext from "../../context/AuthContext";
import { useParams } from "react-router-dom";

const getFullName = (u) => {
  if (u.firstName && u.lastName) return u.firstName + " " + u.lastName;
  return u.firstName || u.userName;
};

const Profile = () => {
  let { userId } = useParams();
  const { user, getMe, setUser, getOtherUser } = useContext(AuthContext);

  useEffect(() => {
    if (userId === undefined) {
      setUser(getMe());
    } else {
      (async () =>getOtherUser(userId))();
    }
  }, [userId]);

  return (
    <div className="profile">
      <Navbar />
      <div className="profileWrapper">
        <Sidebar />
        <div className="profileRight">
          <div className="profileRightTop">
            <div className="profileCover">
              <img
                src="/assets/profileCover/profilecover.jpg"
                alt=""
                className="profileCoverImg"
              />
              <img
                src={`http://localhost:6060/facebook/user-api/user/avatar/${user.userId}?${user.avatarId}`}
                alt="User_Image"
                className="profileUserImg"
                onError={({ currentTarget }) => {
                  currentTarget.onerror = null;
                  currentTarget.src = "/assets/profileCover/DefaultProfile.jpg";
                }}
              />
            </div>
            <div className="profileInfo">
              <h4 className="profileInfoName">{getFullName(user)}</h4>
              <span className="profileInfoDesc">Hi Friends!</span>
            </div>
          </div>
          <div className="profileRightBottom">
            <Feed />
            <Rightbar profile />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;
