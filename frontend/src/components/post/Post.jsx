import React from "react";
import "./post.scss";
import { IconButton } from "@mui/material";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import { Link } from "react-router-dom";

const sinceTime = (postTime) => {
  let diff = Date.now() - new Date(postTime).getTime();
  let daydiff = Math.floor(diff / (1000 * 60 * 60 * 24));
  if (daydiff > 0)
    return `${daydiff} days ago`;

  return `${Math.floor(diff / (1000 * 60 * 60))} hrs ago`
}

const Post = ({ post }) => {
  return (
    <div className="post">
      <div className="postWrapper">

        <div className="postTop">
          <div className="postTopLeft">
            <Link to="/profile">
              <img
                src={
                  "http://localhost:6060/facebook/user-api/user/avatar/" +
                  post.user.userId
                }
                alt="User Profile"
                className="postProfileImg"
                onError={({ currentTarget }) => {
                  currentTarget.onerror = null;
                  currentTarget.src = "/assets/profileCover/DefaultProfile.jpg";
                }}
              />
            </Link>
            <span className="postUsername">
              {post.user.userName}
            </span>
            <span className="postDate">{sinceTime(post.postTime)}</span>
          </div>
          <div className="postTopRight">
            <IconButton>
              <MoreVertIcon className="postVertButton" />
            </IconButton>
          </div>
        </div>
        <div className="postCenter">
          <span className="postText">{post.description}</span>
          {post.postImageId && <img
                src={
                  "http://localhost:6060/facebook/post-api/post/postImage/" +
                  post.postId
                }
                alt=""
                className="postImg" 
                onError={({ currentTarget }) => {
                  currentTarget.onerror = null;
                  currentTarget.src = "";
                }}
              /> }
        </div>
      </div>
    </div>
  );
};

export default Post;
