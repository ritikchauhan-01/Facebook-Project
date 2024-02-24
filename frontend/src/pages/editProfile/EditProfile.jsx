import DriveFolderUploadOutlinedIcon from "@mui/icons-material/DriveFolderUploadOutlined";
import CloseIcon from "@mui/icons-material/Close";
import React, { useState, useRef, useContext } from "react";
import Navbar from "../../components/navbar/Navbar";
import Sidebar from "../../components/sidebar/Sidebar";
import "./editProfile.scss";
import AuthContext from "../../context/AuthContext";

const getFullName = (u) => {
  if (u.firstName && u.lastName) return u.firstName + " " + u.lastName;
  return u.firstName || u.userName;
};

const EditProfile = () => {
  const form = useRef();
  const checkBtn = useRef();
  const { user, editProfile } = useContext(AuthContext);

  const [firstName, setFirstName] = useState(user.firstName);
  const [lastName, setLastName] = useState(user.lastName);
  const [userName, setUserName] = useState(user.userName);
  const [phoneNumber, setPhoneNumber] = useState(user.contactNumber);
  const [address, setAddress] = useState(user.address);
  const [country, setCountry] = useState(user.country);
  const [file, setFile] = useState(null);
  const [successful, setSuccessful] = useState(false);
  const [message, setMessage] = useState("");

  const removeImage = () => {
    setFile(null);
  };

  const handleRegister = async (e) => {
    e.preventDefault();

    setMessage("");
    setSuccessful(false);

    try {
      await editProfile(
        userName,
        firstName,
        lastName,
        phoneNumber,
        address,
        country,
        file
      );
      setMessage("Profile Updated successfully");
      setSuccessful(true);
      setFile(null);
    } catch (error) {
      const resMessage =
        (error.response &&
          error.response.data &&
          error.response.data.message) ||
        error.message ||
        error.toString();

      setMessage(resMessage);
      setSuccessful(false);
      setFile(null);
    }
  };

  return (
    <div className="editProfile">
      <Navbar />
      <div className="editProfileWrapper">
        <Sidebar />
        <div className="profileRight">
          <div className="profileRightTop">
            <div className="profileCover">
              <img
                src="/assets/profileCover/profilecover.jpg"
                alt=""
                className="profileCoverImg"
              />
              <img
                src={`http://localhost:6060/facebook/user-api/user/avatar/${user.userId}?${user.avatarId}`}
                alt="User_Image"
                className="profileUserImg"
                onError={({ currentTarget }) => {
                  currentTarget.onerror = null;
                  currentTarget.src = "/assets/profileCover/DefaultProfile.jpg";
                }}
              />
            </div>
            <div className="profileInfo">
              <h4 className="profileInfoName">{getFullName(user)}</h4>
              <span className="profileInfoDesc">Hi Friends!</span>
            </div>
          </div>
          <div className="editprofileRightBottom">
            <div className="top">
              <h1>Edit User Profile</h1>
            </div>
            <div className="bottom">
              <div className="left">
                <img
                  src={
                    file
                      ? URL.createObjectURL(file)
                      : "/assets/profileCover/DefaultProfile.jpg"
                  }
                  alt=""
                />
              </div>
              <div className="right">
                <form onSubmit={handleRegister} ref={form}>
                  <div className="formInput">
                    <div className="imageInputLine">
                      <div className="imageInputText">Image: </div>
                      {file ? (
                        <CloseIcon
                          className="icon"
                          onClick={removeImage}
                          style={{ color: "#ff605c" }}
                        />
                      ) : (
                        <label htmlFor="file">
                          <DriveFolderUploadOutlinedIcon className="icon" />
                        </label>
                      )}
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
                    </div>
                  </div>
                  <div className="formInput">
                    <label>First Name</label>
                    <input
                      type="text"
                      placeholder="Jane"
                      value={firstName}
                      onChange={(e) => setFirstName(e.target.value)}
                    />
                  </div>
                  <div className="formInput">
                    <label>Last Name</label>
                    <input
                      type="text"
                      placeholder="Doe"
                      value={lastName}
                      onChange={(e) => setLastName(e.target.value)}
                    />
                  </div>
                  <div className="formInput">
                    <label>Username</label>
                    <input
                      type="text"
                      placeholder="jane_doe"
                      value={userName}
                      onChange={(e) => setUserName(e.target.value)}
                    />
                  </div>
                  <div className="formInput">
                    <label>Phone</label>
                    <input
                      type="text"
                      placeholder="+4 123 456 789"
                      value={phoneNumber}
                      onChange={(e) => setPhoneNumber(e.target.value)}
                    />
                  </div>
                  <div className="formInput">
                    <label>Address</label>
                    <input
                      type="text"
                      placeholder="My Address"
                      value={address}
                      onChange={(e) => setAddress(e.target.value)}
                    />
                  </div>
                  <div className="formInput">
                    <label>Country</label>
                    <input
                      type="text"
                      placeholder="India"
                      value={country}
                      onChange={(e) => setCountry(e.target.value)}
                    />
                  </div>
                  {message && (
                    <div className="form-group">
                      <div
                        className={
                          successful
                            ? "alert alert-success"
                            : "alert alert-danger"
                        }
                      >
                        {message}
                      </div>
                    </div>
                  )}
                  <button type="submit" className="updateButton" ref={checkBtn}>
                    Update Profile
                  </button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EditProfile;
