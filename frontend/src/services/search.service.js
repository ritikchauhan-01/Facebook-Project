import axios from "axios";

const searchUsers = async (keyword) => {
  let response = await axios.get(
    `http://localhost:6060/facebook/user-api/user/search`,
    {
      params: {
        keyword,
      },
      headers: {
        Authorization: JSON.parse(localStorage.getItem("jwtToken")).accessToken,
      },
    }
  );

  return response.data
};

const searchPosts = async (keyword) => {
  let response = await axios.get(
    `http://localhost:6060/facebook/post-api/posts/search`,
    {
      params: {
        keyword,
      },
      headers: {
        Authorization: JSON.parse(localStorage.getItem("jwtToken")).accessToken,
      },
    }
  );

  return response.data
}

const SearchService = {
  searchUsers,
  searchPosts
};

export default SearchService;
