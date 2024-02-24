import React, { useContext, useEffect, useState } from "react";
import { Link } from "react-router-dom";
import PersonAddIcon from "@mui/icons-material/PersonAdd";
import CloseIcon from "@mui/icons-material/Close";
import DoneIcon from "@mui/icons-material/Done";
import "./profileRightBar.scss";
import Online from "../online/Online";
import FriendsService from "../../services/friends.service";
import AuthContext from "../../context/AuthContext";

// const delay = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

const ProfileRightBar = () => {
  const { user, getMe } = useContext(AuthContext);
  const [friends, setFriends] = useState(null);
  const [fStatus, setFStatus] = useState(null);
  const [fLoader, setFLoaded] = useState(false);

  useEffect(() => {
    setFLoaded(false);
    const func = async () => {
      let data = await FriendsService.getFriendList(user.userId, 0, 9);
      setFriends(data);
      setFLoaded(true);
    };
    func();
    // FriendsService.getFriendList(user.userId, 0, 9).then(data => setFriends(data))
  }, [user]);

  useEffect(() => {
    let func = async () => {
      // await delay(100);
      if (fLoader) {
        if (user.userId === getMe().userId) setFStatus("M");
        else if (friends.find((f) => f.userId === getMe().userId) !== undefined)
          setFStatus("F");
        else setFStatus(await FriendsService.checkFriendRequest(user.userId));
      }
    };
    func();
  }, [friends, user, fLoader, getMe]);

  const friendRequestHandler = async () => {
    try {
      await FriendsService.sendFriendRequest(user.userId);
      setFStatus("TO");
    } catch (error) {
      const resMessage =
        (error.response &&
          error.response.data &&
          error.response.data.message) ||
        error.message ||
        error.toString();
      console.log(resMessage);
    }
  };

  const acceptRequestHandler = async () => {
    try {
      await FriendsService.acceptFriendRequest(user.userId);
      setFStatus("F");
    } catch (error) {
      const resMessage =
        (error.response &&
          error.response.data &&
          error.response.data.message) ||
        error.message ||
        error.toString();
      console.log(resMessage);
    }
  };

  const withdrawRequestHandler = async () => {
    try {
      await FriendsService.withdrawFriendRequest(user.userId);
      setFStatus(null);
    } catch (error) {
      const resMessage =
        (error.response &&
          error.response.data &&
          error.response.data.message) ||
        error.message ||
        error.toString();
      console.log(resMessage);
    }
  };

  return (
    <div className="profileRightBar">
      <div className="profileRightBarHeading">
        <span className="profileRightBarTitle"> User Information</span>

        {fStatus === "M" && (
          <Link to="/profile/edit" style={{ textDecoration: "none" }}>
            <span className="editButton">Edit Profile</span>
          </Link>
        )}
        {fStatus === null && (
          <span className="addFriendButton" onClick={friendRequestHandler}>
            <PersonAddIcon />
            <span className="addFriendText">Friend</span>
          </span>
        )}

        {fStatus === "FROM" && (
          <span className="acceptFriendButton" onClick={acceptRequestHandler}>
            <DoneIcon />
            <span className="addFriendText">Accept</span>
          </span>
        )}

        {fStatus === "TO" && (
          <span
            className="withdrawFriendButton"
            onClick={withdrawRequestHandler}
          >
            <CloseIcon />
            <span className="addFriendText">WithDraw</span>
          </span>
        )}
      </div>

      <div className="profileRightBarInfo">
        <div className="profileRightBarInfoItem">
          <span className="profileRightBarInfoKey">Email:</span>
          <span className="profileRightBarInfoValue">{user.emailId}</span>
        </div>
        {user.contactNumber && (
          <div className="profileRightBarInfoItem">
            <span className="profileRightBarInfoKey">Phone Number:</span>
            <span className="profileRightBarInfoValue">
              {user.contactNumber}
            </span>
          </div>
        )}

        {user.address && (
          <div className="profileRightBarInfoItem">
            <span className="profileRightBarInfoKey">Address:</span>
            <span className="profileRightBarInfoValue">{user.address}</span>
          </div>
        )}

        {user.country && (
          <div className="profileRightBarInfoItem">
            <span className="profileRightBarInfoKey">Country:</span>
            <span className="profileRightBarInfoValue">{user.country}</span>
          </div>
        )}
      </div>

      <h4 className="profileRightBarTitle">Friends</h4>
      <div className="profileRightBarFollowings">
        {friends && (
          <>
            {friends.map((u) => (
              <Online key={u.userId} user={u} />
            ))}
          </>
        )}
      </div>
    </div>
  );
};

export default ProfileRightBar;
