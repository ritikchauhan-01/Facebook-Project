import { BrowserRouter, Routes, Route } from "react-router-dom";
import "./style/dark.scss";
import Home from "./pages/home/Home";
import Profile from "./pages/profile/Profile";
import EditProfile from "./pages/editProfile/EditProfile";
import Register from "./pages/register/Register";
import Login from "./pages/login/Login";
import { useContext } from "react";
import { DarkModeContext } from "./context/darkModeContext";
import { AuthContextProvider } from "./context/AuthContext";

function App() {
  const { darkMode } = useContext(DarkModeContext);
  return (
    <div className={darkMode ? "app dark" : "app"}>
      <BrowserRouter>
        <AuthContextProvider>
          <Routes>
            <Route path="/">
              <Route path="login" element={<Login />} />
              <Route path="register" element={<Register />} />
              <Route index element={<Home />} />
              <Route path="profile" element={<Profile />} />
              <Route path="profile/:userId" element={<Profile />} />
              <Route path="profile/edit" element={<EditProfile />} />
            </Route>
          </Routes>
        </AuthContextProvider>
      </BrowserRouter>
    </div>
  );
}

export default App;
