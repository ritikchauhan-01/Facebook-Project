import React, { useContext } from "react";
import "./menuLink.scss";
import AuthContext from "../../context/AuthContext";

const getName = (u) => {
  return u.firstName || u.userName;
};

const MenuLink = ({ Icon, text }) => {
  const { getMe } = useContext(AuthContext);
  return (
    <div className="menuLink">
      {Icon}
      <span className="menuLinkText">{text}</span>
      {/* Virat is a pseudo user, replace from redux store */}
      <span className="menuLinkTextName">
        {text === "Logout" && `(${getName(getMe())})`}
      </span>
    </div>
  );
};

export default MenuLink;
