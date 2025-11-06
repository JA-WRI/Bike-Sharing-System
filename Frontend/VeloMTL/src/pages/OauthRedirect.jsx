import { useEffect, useContext } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";

export default function OauthRedirect() {
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();
  const { search } = useLocation();

  useEffect(() => {
    const params = new URLSearchParams(search);
    const token = params.get("token");
    const name = params.get("name");
    const email = params.get("email");
    const role = params.get("role") || "RIDER";

    if (token) {
      // log in via your existing AuthContext.login (same shape used by Login/Register)
      login(token, { name, email, role });

      // Replace history entry so token won't stay in browser history
      navigate("/", { replace: true });
    } else {
      // if no token, redirect to login page
      navigate("/login", { replace: true });
    }
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []); // run once on mount

  return <div style={{padding:20}}>Signing you in…</div>;
}
