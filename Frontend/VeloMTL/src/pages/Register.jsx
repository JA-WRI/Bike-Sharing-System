
import React, { useState, useContext } from "react";
import { useNavigate, Link } from "react-router-dom";
import { registerUser } from "../api/authApi";
import { AuthContext } from "../context/AuthContext";
import "../styles/register.css";

const Register = () => {
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);


const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    try {
      const data = await registerUser(name, email, password);
      login(data.token, {name: data.name, email: data.email, role: data.role, id: data.id, tier: "ENTRY" });
      navigate("/"); // go straight to dashboard
    } catch (err) {
      console.error("Registration failed:", err);
      setError("Registration failed. Please try again.");
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
              onChange={(e) => setName(e.target.value)}
              required
            />

            <label className="auth-label">Email</label>
            <input
              className="auth-input"
              type="email"
              name="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />

            <label className="auth-label">Password</label>
            <input
              className="auth-input"
              type="password"
              name="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />

            {error && <p className="auth-error">{error}</p>}

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
