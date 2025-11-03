
import axios from "axios";

export const loginUser = async (email, password, role) => {
  try {
    const endpoint =
      role === "OPERATOR"
        ? "http://localhost:8080/api/auth/operator/login"
        : "http://localhost:8080/api/auth/rider/login";

    const response = await axios.post(
      endpoint,
      { email, password }, // must match backend request shape
      { headers: { "Content-Type": "application/json" } }
    );

    return response.data; // { token, name, email, role }
  } catch (error) {
    console.error("Login error:", error.response || error);
    throw error;
  }
};
