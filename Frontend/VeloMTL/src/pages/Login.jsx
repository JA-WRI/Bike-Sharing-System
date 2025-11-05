import { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { loginUser } from "../api/authApi";
import { AuthContext } from "../context/AuthContext";

export default function Login() {
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("RIDER");
  const [error, setError] = useState(null);
  const [showRoleSelector, setShowRoleSelector] = useState(false); // toggle for dropdown

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

  return (
    <form onSubmit={handleSubmit} style={{ position: "relative" }}>
      <input
        type="email"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        required
      />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        required
      />

      <button
        type="button"
        onClick={() => setShowRoleSelector((prev) => !prev)}
        style={{ marginLeft: "10px" }}
      >
      </button>

      {showRoleSelector && (
        <select value={role} onChange={(e) => setRole(e.target.value)}>
          <option value="RIDER">Rider</option>
          <option value="OPERATOR">Operator</option>
        </select>
      )}

      <button type="submit">Login</button>

      {error && <p style={{ color: "red" }}>{error}</p>}
    </form>
  );
}
