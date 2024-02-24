import React, { useEffect, useState } from "react";
import "./searchbar.scss";
import SearchIcon from "@mui/icons-material/Search";
import SearchService from "../../services/search.service";
import { useNavigate } from "react-router-dom";

const Searchbar = () => {
  const [users, setUsers] = useState([]);
  const [posts, setPosts] = useState([]);
  const [text, setText] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    if (text.length > 0) {
      SearchService.searchUsers(text).then((data) => {
        setUsers(data);
      });
      SearchService.searchPosts(text).then((data) => {
        setPosts(data);
      });
    } else {
      setUsers([]);
      setPosts([]);
    }
  }, [text]);

  const onChangeHandler = (text) => {
    setText(text);
  };

  return (
    <div className="searchBar_wrapper">
      <div className="searchBar">
        <SearchIcon className="searchIcon" />
        <input
          type="text"
          placeholder="Search for friends or posts"
          className="searchInput"
          onChange={(e) => onChangeHandler(e.target.value)}
          value={text}
        />
      </div>
      {(users.length > 0 || posts.length > 0) && (
        <div className="autocom-box">
          {users.length > 0 && (
            <>
              <div className="search_tag">Users</div>
              {users.map((u) => (
                <li
                  className="search_item"
                  key={u.userId}
                  onClick={() => {
                    setText("");
                    navigate(`/profile/${u.userId}`);
                  }}
                >
                  <div className="search_img">
                    <img
                      src={
                        "http://localhost:6060/facebook/user-api/user/avatar/" +
                        u.userId
                      }
                      alt="User_Image"
                      onError={({ currentTarget }) => {
                        currentTarget.onerror = null;
                        currentTarget.src =
                          "/assets/profileCover/DefaultProfile.jpg";
                      }}
                    />
                  </div>
                  <div className="search_info">
                    <p>
                      <span>{u.userName}</span> - <i>{u.emailId}</i>
                    </p>
                  </div>
                </li>
              ))}
            </>
          )}

          {posts.length > 0 && (
            <>
              <div className="search_tag">Posts</div>
              {posts.map((p) => (
                <li
                  key={p.postId}
                  className="search_item"
                  onClick={() => {
                    setText("");
                    navigate(`/profile/${p.user.userId}`);
                  }}
                >
                  {p.postImageId && (
                    <div className="search_img_post">
                      <img
                        src={
                          "http://localhost:6060/facebook/post-api/post/postImage/" +
                          p.postId
                        }
                        alt="Post_Image"
                        onError={({ currentTarget }) => {
                          currentTarget.onerror = null;
                          currentTarget.src =
                            "/assets/profileCover/DefaultProfile.jpg";
                        }}
                      />
                    </div>
                  )}
                  <div className="search_info">
                    <p>
                      {p.description} - <span><i>{p.user.userName}</i></span>
                    </p>
                  </div>
                </li>
              ))}
            </>
          )}
        </div>
      )}
    </div>
  );
};

export default Searchbar;
