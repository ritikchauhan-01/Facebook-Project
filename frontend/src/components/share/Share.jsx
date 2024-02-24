import React, { useContext, useState } from "react";
import CloseIcon from "@mui/icons-material/Close";
import PermMediaIcon from "@mui/icons-material/PermMedia";
import "./share.scss";
import PostService from "../../services/post.service";
import { useNavigate } from "react-router-dom";
import AuthContext from "../../context/AuthContext";

const getName = (u) => {
  return u.firstName || u.userName;
};

const Share = () => {
  const { user } = useContext(AuthContext);
  const [file, setFile] = useState(null);
  const [description, setDescription] = useState("");
  const navigate = useNavigate();

  const removeImage = () => {
    setFile(null);
  };

  const handleShare = () => {
    PostService.createPost(description, file).then((response) => {
      setDescription("");
      setFile(null);
      navigate("/profile");
    });
  };

  return (
    <div className="share">
      <div className="shareWrapper">
        <div className="shareTop">
          <img
            src={`http://localhost:6060/facebook/user-api/user/avatar/${user.userId}?${user.avatarId}`}
            alt="User_Image"
            className="shareProfileImg"
            onError={({ currentTarget }) => {
              currentTarget.onerror = null;
              currentTarget.src = "/assets/profileCover/DefaultProfile.jpg";
            }}
          />
          <input
            type="text"
            placeholder={`What's on your mind ${getName(user)} ?`}
            className="shareInput"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </div>
        <hr className="shareHr" />
        {file && (
          <div className="shareImgContainer">
            <img src={URL.createObjectURL(file)} alt="" className="shareImg" />
            <CloseIcon className="shareCancelImg" onClick={removeImage} />
          </div>
        )}
        <div className="shareBottom">
          <div className="shareOptions">
            <label htmlFor="file" className="shareOption">
              <PermMediaIcon
                className="shareIcon"
                style={{ color: "#2e0196f1" }}
              />
              <span className="shareOptionText">Photo/Video</span>
              <input
                type="file"
                id="file"
                accept=".png,.jpeg,.jpg"
                style={{ display: "none" }}
                onChange={(e) => {
                  setFile(e.target.files[0]);
                  e.target.value = null;
                }}
              />
            </label>
            <div className="shareOption">
              <button
                className="shareButton"
                disabled={description || file ? false : true}
                onClick={handleShare}
              >
                Share
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Share;
