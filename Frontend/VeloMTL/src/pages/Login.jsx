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

  const capitaliseStr = (str) => str?.toLowerCase().replace(/^./, (match) => match.toUpperCase());

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    try {
      const data = await loginUser(email, password, role);

      // Get tier information from login response (tierChange is included in the response)
      const tierChange = data.tierChange || {};
      const newTier = tierChange.newTier || data.tier;
      
      login(data.token, { 
        id: data.id, 
        email: data.email, 
        name: data.name, 
        role: data.role, 
        tier: newTier 
      });
      
      // Navigate to dashboard first, then show tier change notification if applicable
      // Pass tier change info through navigation state and localStorage as backup
      // Jackson serializes isTierChanged() as "tierChanged" in JSON
      const hasTierChange = (tierChange.tierChanged === true || tierChange.isTierChanged === true) 
        && tierChange.oldTier 
        && tierChange.newTier;
      
      const tierChangeInfo = hasTierChange
        ? { oldTier: tierChange.oldTier, newTier: tierChange.newTier }
        : null;
      
      // Store in localStorage as backup in case navigation state doesn't persist
      if (tierChangeInfo) {
        localStorage.setItem("pendingTierChange", JSON.stringify(tierChangeInfo));
      }
      
      navigate("/", { state: { tierChange: tierChangeInfo } });
    } catch (err) {
      setError("Login failed. Check your credentials.");
      console.error("Login failed:", err);
    }
  };

  const handleGoogle = () => {
    window.location.href = "http://localhost:8080/oauth2/authorization/google";
  };

  return (
    <div className="login-shell">
      <div className="login-card">
        {/* LEFT PANEL */}
        <div className="login-side">
          <h2>VeloMTL</h2>
          <p>Smart bike sharing for Montreal. Monitor trips, riders and docks.</p>
        </div>

        {/* RIGHT PANEL */}
        <div className="login-main">
          <div className="login-header">
            <h1>Welcome back</h1>
            <p>Sign in to continue to the dashboard.</p>
          </div>

          <form className="login-form" onSubmit={handleSubmit}>
            <label className="login-label">Email</label>
            <input
              className="login-input"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />

            <label className="login-label">Password</label>
            <input
              className="login-input"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />

            <label className="login-label">Role</label>
            <select
              className="login-input"
              value={role}
              onChange={(e) => setRole(e.target.value)}
            >
              <option value="RIDER">Rider</option>
              <option value="OPERATOR">Operator</option>
            </select>

            {error && <p className="login-error">{error}</p>}

            <button type="submit" className="login-btn">
              Sign in
            </button>
          </form>

          <div className="login-divider">
            <span>or continue with</span>
          </div>

          <button type="button" className="login-google-btn" onClick={handleGoogle}>
            <span className="google-icon">G</span>
            Google
          </button>

          <p className="login-footer">
            Don&apos;t have an account? <Link to="/register">Create account</Link>
          </p>
        </div>
      </div>
    </div>
  );
}
