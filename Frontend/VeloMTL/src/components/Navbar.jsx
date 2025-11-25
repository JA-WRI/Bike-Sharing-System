import React, { useState, useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import { FaBell, FaUserCircle, FaTimes } from "react-icons/fa";
import "../styles/Navbar.css";
import useOperatorNotifications from "../hook/useOperatorNotifications";

const Navbar = () => {
  const navigate = useNavigate();
  const { user, activeRole, logout, toggleRole } = useContext(AuthContext);
  const [showDropdown, setShowDropdown] = useState(false);

  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const [isBellOpen, setIsBellOpen] = useState(false);

  // Hook to receive operator notifications via WebSocket (only when in operator view)
  useOperatorNotifications((message) => {
    if (activeRole === "OPERATOR" && user?.role === "OPERATOR") {
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
        {user && <Link to="/History" className="navbar-link">History</Link>}
        {/* Show Payment Plans for all users (logged in and logged out) */}
        <Link to="/payment-plans" className="navbar-link">
          Payment Plans
        </Link>

        {/* Show Billing only for riders (active role) */}
        {activeRole === "RIDER" && (
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
            {/* Only show bell for operators when in operator view */}
            {activeRole === "OPERATOR" && user?.role === "OPERATOR" && (
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
            {/* Role toggle button for operators */}
            {user?.role === "OPERATOR" && (
              <button
                onClick={toggleRole}
                className="role-toggle-btn"
                style={{
                  padding: "8px 16px",
                  marginRight: "12px",
                  borderRadius: "6px",
                  border: "1px solid #ddd",
                  backgroundColor: activeRole === "OPERATOR" ? "#0066cc" : "#28a745",
                  color: "white",
                  cursor: "pointer",
                  fontSize: "14px",
                  fontWeight: "500",
                  transition: "all 0.2s"
                }}
                title={`Switch to ${activeRole === "OPERATOR" ? "Rider" : "Operator"} view`}
              >
                {activeRole === "OPERATOR" ? "👤 Operator" : "🚴 Rider"}
              </button>
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
                  {/* <span
                    className={`user-role-badge ${
                      activeRole === "OPERATOR" ? "operator" : "rider"
                    }`}
                  >
                    {activeRole}
                  </span> */}
                  {/* {activeRole === "RIDER" && user.tier && (
                    <span
                      className={`user-role-badge ${user.tier.toLowerCase()}`}
                    >
                      {user.tier}
                    </span>
                  )} */}
                </div>
                <p className="user-email">{user.email}</p>
                <div style={{ 'display': 'flex', 'gap': '8px' }}>
                  <span
                    className={`user-role-badge ${
                      activeRole === "OPERATOR" ? "operator" : "rider"
                    }`}
                  >
                    {activeRole}
                  </span>
                  {activeRole === "RIDER" && user.tier && (
                    <span
                      className={`user-role-badge ${user.tier.toLowerCase()}`}
                    >
                      {user.tier}
                    </span>
                  )}
                </div>
                {user.role === "OPERATOR" && activeRole !== user.role && (
                  <p style={{ fontSize: "0.85rem", color: "#666", marginTop: "4px" }}>
                    Viewing as {activeRole}
                  </p>
                )}
              </div>
                 {/* Add Payment button only for riders (active role) */}
                                  {activeRole === "RIDER" && (
                                    <button onClick={() => {
                                      setShowDropdown(false);
                                      navigate("/add-payment");
                                    }}>
                                      Add Payment
                                    </button>
                                  )}
              {/* Account button only for riders (active role) */}
              {activeRole === "RIDER" && (
                <button onClick={() => {
                  setShowDropdown(false);
                  navigate("/account");
                }}>
                  Account
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
