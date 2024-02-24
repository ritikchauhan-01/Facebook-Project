import React, { useContext, useEffect, useState } from "react";
import RssFeedIcon from "@mui/icons-material/RssFeed";
import PeopleAltIcon from "@mui/icons-material/PeopleAlt";
import GroupsIcon from "@mui/icons-material/Groups";
import Brightness4Icon from "@mui/icons-material/Brightness4";
import ExitToAppOutlinedIcon from "@mui/icons-material/ExitToAppOutlined";

import "./sidebar.scss";
import MenuLink from "../menuLink/MenuLink";
import Friends from "../friends/Friends";
import { DarkModeContext } from "./../../context/darkModeContext";
import { useNavigate } from "react-router-dom";
import FriendsService from "../../services/friends.service";
import AuthContext from "../../context/AuthContext";

const Sidebar = () => {
  const { dispatch } = useContext(DarkModeContext);
  const { getMe, logout, setUser } = useContext(AuthContext);
  const navigate = useNavigate();
  const [friends, setFriends] = useState(null);

  useEffect(() => {
    FriendsService.getFriendList(getMe().userId, 0, 10).then((data) =>
      setFriends(data)
    );
  }, []);

  return (
    <div className="sidebar">
      <div className="sidebarWrapper">
        <span
          onClick={() => {
            setUser(getMe());
            navigate("/");
          }}
        >
          <MenuLink Icon={<RssFeedIcon />} text="Feed" />
        </span>
        <MenuLink Icon={<GroupsIcon />} text="Groups" />
        <span
          onClick={() => {
            setUser(getMe());
            navigate("/profile");
          }}
        >
          <MenuLink Icon={<PeopleAltIcon />} text="Friends" />
        </span>

        <span onClick={() => dispatch({ type: "TOGGLE" })}>
          <MenuLink Icon={<Brightness4Icon />} text="Theme" />
        </span>

        <span
          onClick={async () => {
            await logout();
          }}
        >
          <MenuLink Icon={<ExitToAppOutlinedIcon />} text="Logout" />
        </span>

        {/* <button className="sidebarButton">Show More</button> */}
        <hr className="sidebarHr" />

        {friends && (
          <ul className="sidebarFriendList">
            {friends.map((u) => (
              <Friends key={u.userId} user={u} />
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};

export default Sidebar;
