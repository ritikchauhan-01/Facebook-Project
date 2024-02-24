import { useState } from "react";
import { createContext } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const AuthContext = createContext();

export const AuthContextProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    let user = localStorage.getItem("user");
    if (user) {
      return JSON.parse(user);
    }
    return null;
  });

  const navigate = useNavigate();

  const login = async (emailId, password) => {
    let jwtToken = await axios.post(
      "http://localhost:6060/facebook/user-api/user/login",
      {
        emailId,
        password,
      }
    );
    localStorage.setItem("jwtToken", JSON.stringify(jwtToken.data));

    let user = await axios.get(
      "http://localhost:6060/facebook/user-api/user/me",
      {
        headers: {
          Authorization: JSON.parse(localStorage.getItem("jwtToken"))
            .accessToken,
        },
      }
    );
    localStorage.setItem("user", JSON.stringify(user.data));
    setUser(user.data);
    navigate("/");
  };

  const register = async (userName, emailId, password, file) => {
    let jwtToken = await axios.post(
      "http://localhost:6060/facebook/user-api/user/register",
      {
        userName,
        emailId,
        password,
      }
    );
    localStorage.setItem("jwtToken", JSON.stringify(jwtToken.data));

    let user = await axios.get(
      "http://localhost:6060/facebook/user-api/user/me",
      {
        headers: {
          Authorization: JSON.parse(localStorage.getItem("jwtToken"))
            .accessToken,
        },
      }
    );
    localStorage.setItem("user", JSON.stringify(user.data));
    setUser(user.data);

    if (file !== null) {
      await axios.post(
        "http://localhost:6060/facebook/user-api/user/avatar",
        {
          avatar: file,
        },
        {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: JSON.parse(localStorage.getItem("jwtToken"))
              .accessToken,
          },
        }
      );
    }

    navigate("/");
  };

  const editProfile = async (
    userName,
    firstName,
    lastName,
    contactNumber,
    address,
    country,
    file
  ) => {
    let user = await axios.put(
      "http://localhost:6060/facebook/user-api/user/update-profile",
      {
        userName,
        firstName,
        lastName,
        contactNumber,
        address,
        country,
      },
      {
        headers: {
          Authorization: JSON.parse(localStorage.getItem("jwtToken"))
            .accessToken,
        },
      }
    );

    if (file !== null) {
      user = await axios.post(
        "http://localhost:6060/facebook/user-api/user/avatar",
        {
          avatar: file,
        },
        {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: JSON.parse(localStorage.getItem("jwtToken"))
              .accessToken,
          },
        }
      );
    }

    localStorage.removeItem("user");
    localStorage.setItem("user", JSON.stringify(user.data));
    setUser(user.data);
  };

  const logout = async () => {
    localStorage.removeItem("jwtToken");
    localStorage.removeItem("user");
    setUser(null);
    navigate("/login");
  };

  const getMe = () => {
    return JSON.parse(localStorage.getItem("user"));
  };

  const getOtherUser = async (userId) => {
    let user = await axios.get(
      `http://localhost:6060/facebook/user-api/user/${userId}`,
      {
        headers: {
          Authorization: JSON.parse(localStorage.getItem("jwtToken"))
            .accessToken,
        },
      }
    );
    setUser(user.data);
  };

  return (
    <>
      <AuthContext.Provider
        value={{
          user,
          login,
          register,
          editProfile,
          logout,
          getMe,
          setUser,
          getOtherUser,
        }}
      >
        {children}
      </AuthContext.Provider>
    </>
  );
};

export default AuthContext;
