import axios from "axios";

const getNotifications = async (pageNumber, pageSize) => {
  let response = await axios.get(
    `http://localhost:6060/facebook/notification-api/notifications/${pageNumber}/${pageSize}`,
    {
      headers: {
        Authorization: JSON.parse(localStorage.getItem("jwtToken")).accessToken,
      },
    }
  );

  let nList = response.data;

  for (let v of nList) {
    v.from = await axios.get(
      `http://localhost:6060/facebook/user-api/user/${v.fromId}`,
      {
        headers: {
          Authorization: JSON.parse(localStorage.getItem("jwtToken"))
            .accessToken,
        },
      }
    );

    if (v.postId)
      v.post = await axios.get(
        `http://localhost:6060/facebook/post-api/post/${v.postId}`,
        {
          headers: {
            Authorization: JSON.parse(localStorage.getItem("jwtToken"))
              .accessToken,
          },
        }
      );

    if (v.toId)
      v.to = await axios.get(
        `http://localhost:6060/facebook/user-api/user/${v.toId}`,
        {
          headers: {
            Authorization: JSON.parse(localStorage.getItem("jwtToken"))
              .accessToken,
          },
        }
      );
  }

  return nList;
};

const getFRs = async (pageNumber, pageSize) => {
  let response = await axios.get(
    `http://localhost:6060/facebook/friend-api/received/${pageNumber}/${pageSize}`,
    {
      headers: {
        Authorization: JSON.parse(localStorage.getItem("jwtToken")).accessToken,
      },
    }
  );

  return response.data;
};

const NotificationService = {
  getNotifications,
  getFRs,
};

export default NotificationService;
