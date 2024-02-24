import axios from "axios";

const getFriendList = (userId, pageNumber, pageSize) => {
  return axios
    .get(
      `http://localhost:6060/facebook/user-api/user/friends/${userId}/${pageNumber}/${pageSize}`,
      {
        headers: {
          Authorization: JSON.parse(localStorage.getItem("jwtToken"))
            .accessToken,
        },
      }
    )
    .then((response) => {
      return response.data;
    });
};

const sendFriendRequest = async (userId) => {
  let response = await axios.post(
    "http://localhost:6060/facebook/friend-api/request",
    {
      to: {
        userId,
      },
    },
    {
      headers: {
        Authorization: JSON.parse(localStorage.getItem("jwtToken")).accessToken,
      },
    }
  );
  return response.data;
};

const getFriendRequest = async (userId) => {
  let response = await axios.get(
    `http://localhost:6060/facebook/friend-api/status/${userId}`,
    {
      headers: {
        Authorization: JSON.parse(localStorage.getItem("jwtToken")).accessToken,
      },
    }
  );

  if (!response) return undefined;
  return response.data;
};

const checkFriendRequest = async (userId) => {
  let fr = await getFriendRequest(userId);
  if (fr === undefined) return null;
  else if (fr.status === "PENDING" && fr.to.userId === userId) return "TO";
  else if (fr.status === "PENDING" && fr.from.userId === userId) return "FROM";
  return null;
};

const acceptFriendRequest = async (userId) => {
  let fr = await getFriendRequest(userId);
  if (fr === undefined) return;

  let response = await axios.put(
    "http://localhost:6060/facebook/friend-api/accept/" + fr.requestId,
    undefined,
    {
      headers: {
        Authorization: JSON.parse(localStorage.getItem("jwtToken")).accessToken,
      },
    }
  );
  return response.data;
};

const withdrawFriendRequest = async (userId) => {
  let fr = await getFriendRequest(userId);
  if (fr === undefined) return;

  let response = await axios.put(
    "http://localhost:6060/facebook/friend-api/withdraw/" + fr.requestId,
    undefined,
    {
      headers: {
        Authorization: JSON.parse(localStorage.getItem("jwtToken")).accessToken,
      },
    }
  );
  return response.data;
};

const FriendsService = {
  getFriendList,
  sendFriendRequest,
  getFriendRequest,
  checkFriendRequest,
  acceptFriendRequest,
  withdrawFriendRequest,
};

export default FriendsService;
