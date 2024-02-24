import React, { useRef, useState, useContext } from "react";
import "./register.scss";
import DriveFolderUploadOutlinedIcon from "@mui/icons-material/DriveFolderUploadOutlined";
import CloseIcon from "@mui/icons-material/Close";
import { Link } from "react-router-dom";
import { isEmail } from "validator";
import AuthContext from "../../context/AuthContext";

const required = (value) => {
  if (!value) {
    return "This field is required!";
  }
};

const validEmail = (value) => {
  if (!isEmail(value)) {
    return "This is not a valid email.";
  }
};

const vusername = (value) => {
  if (value.length < 3 || value.length > 20) {
    return "The username must be between 3 and 20 characters.";
  }
};

const vpassword = (value) => {
  if (value.length < 6 || value.length > 40) {
    return "The password must be between 6 and 40 characters.";
  }
};

const vconfirmPassword = (value, password) => {
  if (value !== password) {
    return "The password and confirm password should match.";
  }
};

const Register = () => {
  const form = useRef();
  const checkBtn = useRef();

  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [file, setFile] = useState(null);
  const [successful, setSuccessful] = useState(false);
  const [message, setMessage] = useState("");
  const { register } = useContext(AuthContext);

  const onChangeUsername = (e) => {
    const username = e.target.value;
    setUsername(username);
  };

  const onChangeEmail = (e) => {
    const email = e.target.value;
    setEmail(email);
  };

  const onChangePassword = (e) => {
    const password = e.target.value;
    setPassword(password);
  };

  const onChangeConfirmPassword = (e) => {
    const password = e.target.value;
    setConfirmPassword(password);
  };

  const removeImage = () => {
    setFile(null);
  };

  const handleRegister = async (e) => {
    e.preventDefault();

    setMessage("");
    setSuccessful(false);

    let errMsg =
      required(username) ||
      vusername(username) ||
      required(email) ||
      validEmail(email) ||
      required(password) ||
      vpassword(password) ||
      required(confirmPassword) ||
      vconfirmPassword(confirmPassword, password);

    if (errMsg !== undefined) {
      setMessage(errMsg);
      setSuccessful(false);
    } else {
      try {
        await register(username, email, password, file);
      } catch (error) {
        const resMessage =
          (error.response &&
            error.response.data &&
            error.response.data.message) ||
          error.message ||
          error.toString();

        setMessage(resMessage);
        setSuccessful(false);
      }
    }
  };

  return (
    <div className="register">
      <div className="registerWrapper">
        <div className="registerLeft">
          <h3 className="registerLogo">FaceBook</h3>
          <span className="registerDesc">
            Connect with friends and the world around you on Facebook.
          </span>
        </div>
        <div className="registerRight">
          <div className="registerBox">
            <div className="top">
              <img
                src={
                  file
                    ? URL.createObjectURL(file)
                    : "/assets/profileCover/DefaultProfile.jpg"
                }
                alt=""
                className="profileImg"
              />
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
            </div>
            <div className="bottom">
              <form className="bottomBox" onSubmit={handleRegister} ref={form}>
                <input
                  type="text"
                  placeholder="Username"
                  id="username"
                  className="registerInput"
                  value={username}
                  onChange={onChangeUsername}
                />
                <input
                  type="email"
                  placeholder="Email"
                  id="email"
                  className="registerInput"
                  value={email}
                  onChange={onChangeEmail}
                />
                <input
                  type="password"
                  placeholder="Password"
                  id="password"
                  className="registerInput"
                  value={password}
                  onChange={onChangePassword}
                />
                <input
                  type="password"
                  placeholder="Confirm Password"
                  id="confirmPasword"
                  className="registerInput"
                  value={confirmPassword}
                  onChange={onChangeConfirmPassword}
                />
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
                <button type="submit" className="registerButton" ref={checkBtn}>
                  Sign Up
                </button>
                <Link to="/login">
                  <button className="loginRegisterButton">
                    Log into Account
                  </button>
                </Link>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Register;
