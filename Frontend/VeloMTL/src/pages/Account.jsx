import React, { useContext, useEffect, useState } from "react";
import { AuthContext } from "../context/AuthContext";
import { getAccountInfo } from "../api/authApi";
import "../styles/Account.css";

export default function Account() {
  const { user: contextUser, activeRole } = useContext(AuthContext);
  const [accountInfo, setAccountInfo] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchAccountInfo = async () => {
      try {
        setLoading(true);
        const data = await getAccountInfo();
        setAccountInfo(data);
      } catch (err) {
        console.error("Failed to fetch account info:", err);
        setError("Failed to load account information");
      } finally {
        setLoading(false);
      }
    };

    if (contextUser) {
      fetchAccountInfo();
    }
  }, [contextUser]);

  if (loading) {
    return (
      <div className="account-page">
        <div className="account-container">
          <div className="loading">
            <div className="loading-spinner"></div>
            Loading account information...
          </div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="account-page">
        <div className="account-container">
          <div className="error-message">{error}</div>
        </div>
      </div>
    );
  }

  // Use accountInfo from API if available, otherwise fall back to contextUser
  const displayUser = accountInfo || contextUser;
  const isRider = activeRole === "RIDER" || displayUser?.role === "RIDER";

  return (
    <div className="account-page">
      <div className="account-container">
        <div className="account-header">
          <h1 className="account-title">Account Information</h1>
          <p className="account-subtitle">View your account details and preferences</p>
        </div>

        <div className="account-content">
          {/* User Summary Card */}
          <div className="account-card">
            <h2 className="account-card-title">User Details</h2>
            <div className="account-info-grid">
              <div className="account-info-item">
                <span className="account-info-label">Name</span>
                <span className="account-info-value">{displayUser?.name || "N/A"}</span>
              </div>
              <div className="account-info-item">
                <span className="account-info-label">Email</span>
                <span className="account-info-value">{displayUser?.email || "N/A"}</span>
              </div>
              <div className="account-info-item">
                <span className="account-info-label">Role</span>
                <span className="account-info-value">
                  <span className={`role-badge ${(displayUser?.role || "").toLowerCase()}`}>
                    {displayUser?.role || "N/A"}
                  </span>
                </span>
              </div>
            </div>
          </div>

          {/* Flex Dollars Card */}
          <div className="account-card">
            <h2 className="account-card-title">Flex Dollars</h2>
            <div className="flex-dollars-display">
              <span className="flex-dollars-amount">
                ${accountInfo?.flexDollars?.toFixed(2) || "0.00"}
              </span>
              <p className="flex-dollars-description">
                Available balance for trip payments
              </p>
            </div>
          </div>

          {/* Rider Tier Card - Only visible for riders */}
          {isRider && accountInfo?.tier && (
            <div className="account-card">
              <h2 className="account-card-title">Loyalty Tier</h2>
              <div className="tier-display">
                <span className={`tier-badge ${(accountInfo.tier || "").toLowerCase()}`}>
                  {accountInfo.tier}
                </span>
                <p className="tier-description">
                  Your current loyalty tier status
                </p>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

