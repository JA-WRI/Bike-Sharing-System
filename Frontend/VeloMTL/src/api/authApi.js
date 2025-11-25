import api from "./api";
import axios from "axios";


export const loginUser = async (email, password, role) => {
  try {
    const endpoint =
      role === "OPERATOR"
        ? "http://localhost:8080/api/auth/operator/login"
        : "http://localhost:8080/api/auth/rider/login";

    const response = await axios.post(
      endpoint,
      { email, password },
      { headers: { "Content-Type": "application/json" } }
    );

    return response.data; // { token, name, email, role, ... }
  } catch (error) {
    console.error("Login error:", error.response || error);
    throw error;
  }
};

// registration
export const registerUser = async (name, email, password) => {
  const response = await axios.post(
    "http://localhost:8080/api/auth/register",
    { name, email, password },
    { headers: { "Content-Type": "application/json" } }
  );
  return response.data;
};

// get account information
export const getAccountInfo = async () => {
  try {
    const response = await api.get("/api/auth/account");
    return response.data;
  } catch (error) {
    console.error("Error fetching account info:", error);
    throw error;
  }
};
