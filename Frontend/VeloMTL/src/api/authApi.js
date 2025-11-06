
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
