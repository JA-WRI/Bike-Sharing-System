import React, { useContext, useState } from "react";
import "../styles/PaymentPlans.css";
import { AuthContext } from "../context/AuthContext";
import { addPlan } from "../api/planApi"; 

export default function PaymentPlans() {
  const { user } = useContext(AuthContext);
  const [selectedPlan, setSelectedPlan] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSelectPlan = async (plan) => {
    if (!user) {
      alert("Please log in to select a payment plan.");
      return;
    }
    if (user.role === "OPERATOR") {
      alert("Operators cannot select a payment plan.");
      return;
    }
    if (selectedPlan) {
      alert("You already have a plan. You can only change it next year.");
      return;
    }

    try {
      setLoading(true);
      // API call to backend
      const res = await addPlan(user.email, plan);
      console.log(res);
      setSelectedPlan(plan);
      setMessage(`Thank you! Your ${plan} membership was added to your account.`);
    } catch (error) {
      console.error("Failed to add plan:", error);
      setMessage("Something went wrong while adding your plan. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="plans-container">
      <h1 className="plans-title">Choose Your Plan</h1>
      <h1 className="plans-title">Bike and Dock Reservations are free</h1>


      {user?.role === "OPERATOR" && (
        <p style={{ color: "#666", marginBottom: "20px" }}>
          Payment plans are not available for operators.
        </p>
      )}

      <div className="plans-grid">
        <div
          className={`plan-card basic ${
            selectedPlan === "Basic" ? "selected" : ""
          } ${!user || user.role === "OPERATOR" ? "disabled" : ""}`}
          onClick={() => handleSelectPlan("Basic")}
        >
          <h2>Basic</h2>
          <p><strong>Monthly Fee:</strong> $15</p>
          <p><strong>Rate per Minute:</strong> $0.10</p>
          <p><strong>E-bike Surcharge:</strong> $5</p>
        </div>

        <div
          className={`plan-card premium ${
            selectedPlan === "Premium" ? "selected" : ""
          } ${!user || user.role === "OPERATOR" ? "disabled" : ""}`}
          onClick={() => handleSelectPlan("Premium")}
        >
          <h2>Premium</h2>
          <p><strong>Monthly Fee:</strong> $20</p>
          <p><strong>Rate per Minute:</strong> $0.05</p>
          <p><strong>E-bike Surcharge:</strong> $0</p>
        </div>
      </div>

      {/* Loading state */}
      {loading && <p className="loading-message">Saving your plan...</p>}

      {/* Confirmation message */}
      {message && <p className="confirmation-message">{message}</p>}
    </div>
  );
}
