
import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { registerUser } from "../api/authApi";
import "./register.css";

const Register = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
  });
  const [error, setError] = useState(null);
  const [ok, setOk] = useState(null);

  const handleChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setOk(null);
    try {
      await registerUser(formData.name, formData.email, formData.password);
      setOk("Account created! Redirecting to login…");
      setTimeout(() => navigate("/login"), 1000);
    } catch (err) {
      setError(err?.response?.data || "Registration failed.");
    }
  };

  return (
    <div className="auth-shell">
      <div className="auth-card fancy">
        {/* LEFT PANEL */}
        <div className="auth-side reg">
          <h2>Join VeloMTL</h2>
          <p>Create an account to manage your rides and stations.</p>
        </div>

        {/* RIGHT PANEL */}
        <div className="auth-main">
          <div className="auth-header">
            <h1>Create an account</h1>
            <p>Enter your details to get started.</p>
          </div>

          <form className="auth-form" onSubmit={handleSubmit}>
            <label className="auth-label">Full name</label>
            <input
              className="auth-input"
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
            />

            <label className="auth-label">Email</label>
            <input
              className="auth-input"
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
            />

            <label className="auth-label">Password</label>
            <input
              className="auth-input"
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
            />

            {error && <p className="auth-error">{error}</p>}
            {ok && <p className="auth-success">{ok}</p>}

            <button type="submit" className="primary-btn">
              Sign up
            </button>
          </form>

          <p className="auth-footer">
            Already have an account? <Link to="/login">Sign in</Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Register;
