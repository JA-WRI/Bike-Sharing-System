import React, { createContext, useState, useEffect } from "react";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [activeRole, setActiveRole] = useState(null);

  useEffect(() => {
    try {
      const token = localStorage.getItem("token");
      const userData = localStorage.getItem("user");
      if (token && userData && userData !== "undefined") {
        const parsedUser = JSON.parse(userData);
        setUser(parsedUser);
        // Initialize activeRole from localStorage or default to user's role
        const savedActiveRole = localStorage.getItem("activeRole");
        if (savedActiveRole && parsedUser.role === "OPERATOR") {
          setActiveRole(savedActiveRole);
        } else {
          setActiveRole(parsedUser.role);
        }
      }
    } catch (error) {
      console.error("Failed to parse user from localStorage:", error);
      setUser(null);
      setActiveRole(null);
    }
  }, []);

  const login = (token, userData) => {
    localStorage.setItem("token", token);
    localStorage.setItem("user", JSON.stringify(userData));
    setUser(userData);
    // Set activeRole to user's role on login
    setActiveRole(userData.role);
    // Clear any saved activeRole from previous session
    localStorage.removeItem("activeRole");
  };

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    localStorage.removeItem("activeRole");
    setUser(null);
    setActiveRole(null);
  };

  const toggleRole = () => {
    // Only allow role toggle for operators
    if (user && user.role === "OPERATOR") {
      const newRole = activeRole === "OPERATOR" ? "RIDER" : "OPERATOR";
      setActiveRole(newRole);
      localStorage.setItem("activeRole", newRole);
    }
  };

  return (
    <AuthContext.Provider value={{ user, activeRole, login, logout, toggleRole }}>
      {children}
    </AuthContext.Provider>
  );
};
