import React, { useContext, useRef, useState } from "react";
import { Link } from "react-router-dom";
import { isEmail } from "validator";
import "./login.scss";
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

const vpassword = (value) => {
  if (value.length < 6 || value.length > 40) {
    return "The password must be between 6 and 40 characters.";
  }
};

const Login = () => {
  const form = useRef();
  const checkBtn = useRef();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [successful, setSuccessful] = useState(false);
  const [message, setMessage] = useState("");
  const { login } = useContext(AuthContext);

  const onChangeEmail = (e) => {
    const email = e.target.value;
    setEmail(email);
  };

  const onChangePassword = (e) => {
    const password = e.target.value;
    setPassword(password);
  };

  const handleLogin = async (e) => {
    e.preventDefault();

    setMessage("");
    setSuccessful(false);

    let errMsg =
      required(email) ||
      validEmail(email) ||
      required(password) ||
      vpassword(password);

    if (errMsg !== undefined) {
      setMessage(errMsg);
      setSuccessful(false);
    } else {
      try {
        await login(email, password);
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
    <div className="login">
      <div className="loginWrapper">
        <div className="loginLeft">
          <h3 className="loginLogo">FaceBook</h3>
          <span className="loginDesc">
            Connect with friends and the world around you on Facebook.
          </span>
        </div>
        <div className="loginRight">
          <div className="loginBox">
            <div className="bottom">
              <form className="bottomBox" onSubmit={handleLogin} ref={form}>
                <input
                  type="email"
                  placeholder="Email"
                  id="email"
                  className="loginInput"
                  value={email}
                  onChange={onChangeEmail}
                />
                <input
                  type="password"
                  placeholder="Password"
                  id="password"
                  className="loginInput"
                  value={password}
                  onChange={onChangePassword}
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

                <button type="submit" className="loginButton" ref={checkBtn}>
                  Sign In
                </button>
                <Link to="/register">
                  <button className="loginRegisterButton">
                    Create a New Account
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

export default Login;
