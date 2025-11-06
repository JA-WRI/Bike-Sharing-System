
import { useState, useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import { loginUser } from "../api/authApi";
import { AuthContext } from "../context/AuthContext";
import "../styles/login.css";

export default function Login() {
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("RIDER");
  const [error, setError] = useState(null);
  const [showRoleSelector, setShowRoleSelector] = useState(false);

const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    try {
      const data = await loginUser(email, password, role);
      login(data.token, { id: data.id, email: data.email, name: data.name, role: data.role });
      console.log("Login successful:", data);

      navigate("/");
    } catch (err) {
      setError("Login failed. Check your credentials.");
      console.error("Login failed:", err);
    }
  };

  const handleGoogle = () => {
    window.location.href = "http://localhost:8080/oauth2/authorization/google";
  };

  return (
    <div className="auth-shell">
      <div className="auth-card fancy">
        {/* LEFT PANEL */}
        <div className="auth-side">
          <h2>VeloMTL</h2>
          <p>Smart bike sharing for Montreal. Monitor trips, riders and docks.</p>
        </div>

        {/* RIGHT PANEL */}
        <div className="auth-main">
          <div className="auth-header">
            <h1>Welcome back </h1>
            <p>Sign in to continue to the dashboard.</p>
          </div>

          <form className="auth-form" onSubmit={handleSubmit}>
            <label className="auth-label">Email</label>
            <input
              className="auth-input"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />

            <label className="auth-label">Password</label>
            <input
              className="auth-input"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />

            <label className="auth-label">Role</label>
            <select
              className="auth-input"
              value={role}
              onChange={(e) => setRole(e.target.value)}
            >
              <option value="RIDER">Rider</option>
              <option value="OPERATOR">Operator</option>
            </select>

            {error && <p className="auth-error">{error}</p>}

            <button type="submit" className="primary-btn">
              Sign in
            </button>
          </form>

          <div className="auth-divider">
            <span>or continue with</span>
          </div>

          <button type="button" className="google-btn" onClick={handleGoogle}>
            <span className="google-icon">G</span>
            Google
          </button>

          <p className="auth-footer">
            Don&apos;t have an account? <Link to="/register">Create account</Link>
          </p>
        </div>
      </div>
    </div>
  );
}
