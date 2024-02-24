import React from "react";
import "./rightbar.scss";
import RightbarHome from "../rightbarHome/RightbarHome";
import ProfileRightBar from "../profileRightBar/ProfileRightBar";

const Rightbar = ({ profile }) => {
  return (
    <div className="rightbar">
      <div className="rightbarWrapper">
        {profile ? <ProfileRightBar /> : <RightbarHome />}
      </div>
    </div>
  );
};

export default Rightbar;
