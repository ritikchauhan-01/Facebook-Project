import React from "react";
import "./home.scss";
import Navbar from "../../components/navbar/Navbar";
import Sidebar from "../../components/sidebar/Sidebar";
import Feed from "../../components/feed/Feed";
import Rightbar from "../../components/rightbar/Rightbar";
import { Navigate } from "react-router-dom";

const Home = () => {
  if (localStorage.getItem("user") != null) {
    return (
      <div className="home">
        <Navbar />
        <div className="homeContainer">
          <Sidebar />
          <Feed />
          <Rightbar />
        </div>
      </div>
    );
  }

  return <Navigate to="/register" replace />;
};

export default Home;
