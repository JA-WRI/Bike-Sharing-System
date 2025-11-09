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
     logout();
     navigate("/login");
  };

  const handleBellClick = () => {
    setIsBellOpen(!isBellOpen);
    setUnreadCount(0);
  };

  const handleDeleteNotification = (index) => {
    setNotifications((prev) => prev.filter((_, i) => i !== index));
  };

  return (
    <nav className="navbar">
      <div className="navbar-left">
        <Link to="/" className="navbar-link">Dashboard</Link>
        {userRole && <Link to="/History" className="navbar-link">History</Link>}
        {/* Show Payment Plans for all users (logged in and logged out) */}
        <Link to="/payment-plans" className="navbar-link">
          Payment Plans
        </Link>

        {/* Show Billing only for riders */}
        {userRole === "RIDER" && (
          <Link to="/billing" className="navbar-link">
            Billing
          </Link>
        )}
      </div>

      <div className="navbar-right">
        {!user ? (
          <Link to="/login" className="navbar-link">Login</Link>
        ) : (
          <div className="navbar-user-container">
            {/* Only show bell for operators */}
            {userRole === "OPERATOR" && (
              <div className="notification-bell-container" onClick={handleBellClick}>
                <FaBell
                  className="notification-bell-icon"
                />
                {unreadCount > 0 && (
                  unreadCount > 9 ? (
                    <span className="red-dot"></span>
                  ) : (
                    <span className="notification-badge">{unreadCount}</span>
                  )
                )}
                {isBellOpen && (
                <div className="notification-dropdown" onClick={(e) => e.stopPropagation()}>
                  <div className="notification-dropdown-title">
                    <strong>Notifications</strong>
                    {notifications.length > 0 && (
                      <span style={{ 
                        fontSize: "0.85rem", 
                        color: "var(--text-muted)",
                        fontWeight: "500"
                      }}>
                        {notifications.length} {notifications.length === 1 ? 'notification' : 'notifications'}
                      </span>
                    )}
                  </div>
                  <div className="notification-dropdown-content">
                    {notifications.length === 0 ? (
                      <div className="notification-item empty">
                        <div>No notifications</div>
                        <div style={{ fontSize: "0.85rem", marginTop: "0.5rem" }}>
                          You're all caught up!
                        </div>
                      </div>
                    ) : (
                      notifications.map((msg, i) => (
                        <div key={i} className="notification-item">
                          <div className="notification-item-content">
                            {msg}
                          </div>
                          <FaTimes
                            className="delete-notification-icon"
                            onClick={(e) => {
                              e.stopPropagation();
                              handleDeleteNotification(i);
                            }}
                            title="Delete notification"
                          />
                        </div>
                      ))
                    )}
                  </div>
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
                 {/* Add Payment button only for riders */}
                                  {userRole === "RIDER" && (
                                    <button onClick={() => navigate("/add-payment")}>
                                      Add Payment
                                    </button>
                                  )}
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
