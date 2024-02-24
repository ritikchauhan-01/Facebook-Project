import React, { useContext, useEffect, useState } from "react";
import PersonIcon from "@mui/icons-material/Person";
import NotificationsIcon from "@mui/icons-material/Notifications";
import "./navbar.scss";
import { Link } from "react-router-dom";
import AuthContext from "../../context/AuthContext";
import { useStomp } from "../../hooks/usestomp";
import Notification from "../notification/Notification";
import Searchbar from "../seachbar/Searchbar";
import NotificationService from "../../services/notification.service";

const Navbar = () => {
  const { getMe, setUser } = useContext(AuthContext);
  const [frs, setFrs] = useState(0);
  const [notifications, setNotifications] = useState(0);
  setNotifications.bind(this);
  setFrs.bind(this);
  const [dropdown, setDropdown] = useState(null);

  const frCallback = (body) => {
    console.log("Message from friend-request");
    console.log(body);
    setFrs((s) => s + 1);
  };

  const notificationCallback = (body) => {
    console.log("Message from notify");
    console.log(body);
    setNotifications((s) => {
      return s + 1;
    });
  };

  useStomp(
    `/notification-api/received/queue/${getMe().userId}/friend-request`,
    `/notification-api/received/queue/${getMe().userId}/notify`,
    frCallback,
    notificationCallback
  );

  useEffect(() => {
    NotificationService.getFRs(0, 8).then((data) => {
      setFrs(data.length);
    });
  }, []);

  const handleFrDd = () => {
    setFrs(0);
    setDropdown((s) => (s !== "FR" ? "FR" : null));
  };

  const handleNDd = () => {
    setNotifications(0);
    setDropdown((s) => (s !== "N" ? "N" : null));
  };

  return (
    <div className="navbarContainer">
      <div className="navbarLeft">
        <Link
          to="/"
          style={{ textDecoration: "none" }}
          onClick={() => setUser(getMe())}
        >
          <span className="logo">FaceBook</span>
        </Link>
      </div>
      <div className="navbarCenter">
        <Searchbar />
      </div>
      <div className="navbarRight">
        <div className="navbarLinks">
          <Link to="/profile" style={{ textDecoration: "none" }}>
            <span className="navbarLink">Homepage</span>
          </Link>
        </div>
        <div className="navbarIcons">
          <div className="navbarIconItem" onClick={handleFrDd}>
            <PersonIcon />
            {frs !== 0 && <span className="navbarIconBadge">{frs}</span>}
          </div>
          {/* <div className="navbarIconItem">
            <ChatBubbleIcon />
            <span className="navbarIconBadge">10</span>
          </div> */}
          <div className="navbarIconItem" onClick={handleNDd}>
            <NotificationsIcon />
            {notifications !== 0 && (
              <span className="navbarIconBadge">{notifications}</span>
            )}
          </div>
        </div>
        {dropdown && <Notification type={dropdown}></Notification>}
        <Link to="/profile">
          <img
            src={`http://localhost:6060/facebook/user-api/user/avatar/${
              getMe().userId
            }?${getMe().avatarId}`}
            alt="User_Image"
            className="navbarImg"
            onError={({ currentTarget }) => {
              currentTarget.onerror = null;
              currentTarget.src = "/assets/profileCover/DefaultProfile.jpg";
            }}
          />
        </Link>
      </div>
    </div>
  );
};

export default Navbar;
