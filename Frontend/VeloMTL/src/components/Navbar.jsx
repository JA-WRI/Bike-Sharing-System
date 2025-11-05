import React, { useState, useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import { FaBell, FaUserCircle, FaTimes } from "react-icons/fa";
import "../styles/Navbar.css";
import useOperatorNotifications from "../hook/useOperatorNotifications";

const Navbar = () => {
  const navigate = useNavigate();
  const { user, logout } = useContext(AuthContext);
  const [showDropdown, setShowDropdown] = useState(false);

  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const [isBellOpen, setIsBellOpen] = useState(false);

  const userRole = user?.role;

  // Hook to receive operator notifications via WebSocket
  useOperatorNotifications((message) => {
    if (userRole === "OPERATOR") {
      setNotifications((prev) => [message, ...prev]);
      setUnreadCount((prev) => prev + 1);
    }
  });

  const handleLogout = () => {
    setShowDropdown(false);
    logout(navigate);
  };

  const handleBellClick = () => {
    setIsBellOpen(!isBellOpen);
    setUnreadCount(0); // clear red dot when opening
  };

  const handleDeleteNotification = (index) => {
    setNotifications((prev) => prev.filter((_, i) => i !== index));
  };

  return (
    <nav className="navbar">
      <div className="navbar-left">
        <Link to="/" className="navbar-link">Dashboard</Link>
      </div>

      <div className="navbar-right">
        {!user ? (
          <Link to="/login" className="navbar-link">Login</Link>
        ) : (
          <div className="navbar-user-container">

            {/* Only show bell for operators */}
            {userRole === "OPERATOR" && (
              <div className="notification-bell-container">
                <FaBell
                  className="notification-bell-icon"
                  onClick={handleBellClick}
                  
                />
                {unreadCount > 0 && <span className="red-dot"></span>}
                {isBellOpen && (
                <div className="notification-dropdown">
                  <div className="notification-dropdown-title">
                    <strong>Notifications</strong>
                  </div>
                  {notifications.length === 0 ? (
                    <div className="notification-item empty">No notifications</div>
                  ) : (
                    notifications.map((msg, i) => (
                      <div key={i} className="notification-item">
                        {msg}
                        <FaTimes
                          className="delete-notification-icon"
                          onClick={() => handleDeleteNotification(i)}
                        />
                      </div>
                    ))
                  )}
                </div>
              )}
              </div>
            )}
          <div className="navbar-user">
            <FaUserCircle
              className="user-icon"
              onClick={() => setShowDropdown(!showDropdown)}
            />
            {showDropdown && (
            <div className="user-dropdown">
              <div className="user-info">
                <div className="user-header">
                  <p className="user-name"><strong>{user.name}</strong></p>
                  <span
                    className={`user-role-badge ${
                      user.role === "OPERATOR" ? "operator" : "rider"
                    }`}
                  >
                    {user.role}
                  </span>
                </div>
                <p className="user-email">{user.email}</p>
              </div>

              <button className="logout-btn" onClick={handleLogout}>
                Logout
              </button>
            </div>
          )}
            </div>
          </div>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
