import React from "react";
import "./notifyItem.scss";
import { useNavigate } from "react-router-dom";

const sinceTime = (postTime) => {
  let diff = Date.now() - new Date(postTime).getTime();
  let daydiff = Math.floor(diff / (1000 * 60 * 60 * 24));
  if (daydiff > 0) return `${daydiff} days ago`;

  return `${Math.floor(diff / (1000 * 60 * 60))} hrs ago`;
};

const setDataFR = (n) => {
  return {
    userId: n.from.userId,
    desc: n.from.firstName || n.from.userName,
  };
};

const setDataN = (n) => {
  let fromName = n.from.data.firstName || n.from.data.userName;

  const nType = {
    FR_CREATED: {
      userId: n.from.data.userId,
      desc: fromName,
    },
    FR_ACCEPTED: {
      userId: n.to?.data.userId,
      desc: n.to?.data.firstName || n.to?.data.userName,
    },
    FR_CANCEL: {
      userId: n.to?.data.userId,
      desc: n.to?.data.firstName || n.to?.data.userName,
    },
    FR_WITHDRAW: {
      userId: n.from.data.userId,
      desc: fromName,
    },
    P_CREATED: {
      userId: n.from.data.userId,
      desc: fromName,
    },
    U_UPDATED_AVATAR: {
      userId: n.from.data.userId,
      desc: fromName,
    },
    U_UPDATED_PROFILE: {
      userId: n.from.data.userId,
      desc: fromName,
    },
  };

  return nType[n.type];
};

const NotifyItem = ({ n, type }) => {
  const navigate = useNavigate();

  let data;
  if (type === "FR") data = setDataFR(n);
  else if (type === "N") data = setDataN(n);

  return (
    <div
      className="notify_item"
      onClick={() => navigate(`/profile/${data.userId}`)}
    >
      <div className="notify_img">
        <img
          src={
            "http://localhost:6060/facebook/user-api/user/avatar/" + data.userId
          }
          alt="User_Image"
          onError={({ currentTarget }) => {
            currentTarget.onerror = null;
            currentTarget.src = "/assets/profileCover/DefaultProfile.jpg";
          }}
        />
      </div>
      <div className="notify_info">
        <p>
          <span>{data.desc}</span>{" "}
          {n.description || "sent you a friend request"}
        </p>
        <span className="notify_time">
          {sinceTime(n.notificationTime || n.requestTime)}
        </span>
      </div>
    </div>
  );
};

export default NotifyItem;
