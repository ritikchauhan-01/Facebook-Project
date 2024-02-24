import axios from "axios";

const createPost = (description, file) => {
  return axios
    .post(
      "http://localhost:6060/facebook/post-api/post",
      {
        description
      },
      {
        headers: {
          Authorization: JSON.parse(localStorage.getItem("jwtToken"))
            .accessToken,
        },
      }
    )
    .then((response) => {
      if (file !== null)
        return axios.post(
          "http://localhost:6060/facebook/post-api/post/postImage/" +
            response.data.postId,
          {
            postImage: file,
          },
          {
            headers: {
              "Content-Type": "multipart/form-data",
              Authorization: JSON.parse(localStorage.getItem("jwtToken"))
                .accessToken,
            },
          }
        );
    });
};

const getAllPosts = (userId, pageNumber, pageSize) => {
  return axios.get(
    `http://localhost:6060/facebook/post-api/posts/${userId}/${pageNumber}/${pageSize}`,
    {
      headers: {
        Authorization: JSON.parse(localStorage.getItem("jwtToken")).accessToken,
      },
    }
  );
};

const PostService = {
  createPost,
  getAllPosts,
};

export default PostService;
