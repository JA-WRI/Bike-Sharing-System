// src/api/api.js
import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080", // adjust if your backend runs elsewhere
});

// Automatically attach JWT to all /api/** calls
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");

    // Only attach token for secured endpoints
    if (token && config.url.startsWith("/api/")) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

// Global error handling (optional but recommended)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      console.warn("Unauthorized or expired token. Redirecting to login...");
      localStorage.removeItem("token");
      localStorage.removeItem("role");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export default api;
