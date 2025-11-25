import React, { createContext, useState, useEffect } from "react";

// Create context
export const AuthContext = createContext();

// AuthProvider component to wrap around app
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [activeRole, setActiveRole] = useState("RIDER"); // Default to 'RIDER' role

  // On first load, try to get the user and role from localStorage
  useEffect(() => {
    const token = localStorage.getItem("token");
    const userData = localStorage.getItem("user");

    if (token && userData) {
      const parsedUser = JSON.parse(userData);
      setUser(parsedUser);

      const savedActiveRole = localStorage.getItem("activeRole");
      if (savedActiveRole) {
        setActiveRole(savedActiveRole);  // Set role from localStorage if available
      } else {
        setActiveRole(parsedUser.role || "RIDER");  // Default to 'RIDER' if no role is saved
      }
    }
  }, []);  // Run once when the component mounts

  // Handle user login
  const login = (token, userData) => {
    localStorage.setItem("token", token);
    localStorage.setItem("user", JSON.stringify(userData));
    setUser(userData);
    setActiveRole(userData.role || "RIDER");  // Set activeRole from user role or default to 'RIDER'
  };

  // Handle user logout
  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    localStorage.removeItem("activeRole");
    setUser(null);
    setActiveRole("RIDER");  // Reset role to 'RIDER' on logout
  };

  // Toggle between 'RIDER' and 'OPERATOR' roles
  const toggleRole = () => {
    if (user && user.role === "OPERATOR") {
      const newRole = activeRole === "OPERATOR" ? "RIDER" : "OPERATOR";
      setActiveRole(newRole);
      localStorage.setItem("activeRole", newRole);
    }
  };

  // Provide context to children components
  return (
    <AuthContext.Provider value={{ user, activeRole, login, logout, toggleRole }}>
      {children}
    </AuthContext.Provider>
  );
};
