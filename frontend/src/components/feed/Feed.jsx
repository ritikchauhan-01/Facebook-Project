import React, { useContext, useEffect, useState } from "react";
import "./feed.scss";
import Share from "../share/Share";
import Post from "../post/Post";
import PostService from "../../services/post.service";
import AuthContext from "../../context/AuthContext";

const Feed = () => {
  const { user, getMe } = useContext(AuthContext);
  const [posts, setPosts] = useState(null);

  useEffect(() => {
    PostService.getAllPosts(user.userId, 0, 20).then(
      (response) => {
        setPosts(response.data);
      }
    );
  }, [user]);

  return (
    <div className="feed">
      <div className="feedWrapper">
        {user.userId === getMe().userId && <Share />}
        {posts && (
          <>
            {posts.map((p) => (
              <Post key={p.postId} post={p} />
            ))}
          </>
        )}
      </div>
    </div>
  );
};

export default Feed;
