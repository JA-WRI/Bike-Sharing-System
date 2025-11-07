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
    //Check login before allowing plan selection
    if (!user) {
      setMessage("Please log in to select a payment plan.");
      return;
    }

    if (user.role === "OPERATOR") {
      setMessage("Operators cannot select a payment plan.");
      return;
    }

    if (selectedPlan) {
      setMessage("You already have a plan. You can only change it next year.");
      return;
    }

    try {
      setLoading(true);
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

  const isDisabled = user?.role === "OPERATOR";

  return (
    <section className="pp-wrap-modern v2">
      <div className="pp-inner">
        <header className="pp-head-modern">
          <div>
            <h1 className="pp-title-modern">Choose Your Plan</h1>
            <p className="pp-tagline">
              Flexible pricing for riders — pay for what you ride.
            </p>
          </div>
        </header>

        {user?.role === "OPERATOR" && (
          <p className="pp-note-modern">
            Payment plans are not available for operators.
          </p>
        )}

        <div className="pp-grid-modern">
          {/* BASIC PLAN */}
          <button
            className={`pp-card-modern pp-basic-modern ${
              selectedPlan === "Basic" ? "pp-chosen" : ""
            } ${isDisabled ? "pp-disabled" : ""}`}
            onClick={() => handleSelectPlan("Basic")}
            aria-pressed={selectedPlan === "Basic"}
          >
            <div className="pp-ribbon">Popular</div>
            <div className="pp-card-top">
              <div className="pp-card-title">
                <svg className="pp-ico" viewBox="0 0 24 24" aria-hidden>
                  <path
                    d="M4 12h16M7 6h.01M17 6h.01"
                    strokeWidth="1.4"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    fill="none"
                  />
                </svg>
                <div>
                  <h3>Basic</h3>
                  <p className="pp-subtle">Great for casual riders</p>
                </div>
              </div>

              <div className="pp-price-wrap">
                <div className="pp-price-big">$15</div>
                <div className="pp-price-small">/ month</div>
              </div>
            </div>

            <ul className="pp-feat-modern">
              <li><strong>Rate per Minute:</strong> $0.10</li>
              <li><strong>E-bike Surcharge:</strong> $5</li>
              <li><strong>Free reservations:</strong> docks and bikes included</li>
              <li className="muted">Billed monthly to your account</li>
            </ul>

            <div className="pp-card-actions">
              <span className="pp-cta-modern">
                {selectedPlan === "Basic" ? "Subscribed" : "Select"}
              </span>
            </div>

            {selectedPlan === "Basic" && (
              <div className="pp-selected-badge">Selected</div>
            )}
          </button>

          {/* PREMIUM PLAN */}
          <button
            className={`pp-card-modern pp-prem-modern ${
              selectedPlan === "Premium" ? "pp-chosen" : ""
            } ${isDisabled ? "pp-disabled" : ""}`}
            onClick={() => handleSelectPlan("Premium")}
            aria-pressed={selectedPlan === "Premium"}
          >
            <div className="pp-ribbon pp-ribbon-accent">Best value</div>
            <div className="pp-card-top">
              <div className="pp-card-title">
                <svg className="pp-ico" viewBox="0 0 24 24" aria-hidden>
                  <path
                    d="M12 2l3 6 6 .5-4.5 4 1 6L12 16l-6.5 2 1-6L2 8.5 8 8z"
                    strokeWidth="1.2"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    fill="none"
                  />
                </svg>
                <div>
                  <h3>Premium</h3>
                  <p className="pp-subtle">For frequent riders</p>
                </div>
              </div>

              <div className="pp-price-wrap">
                <div className="pp-price-big">$20</div>
                <div className="pp-price-small">/ month</div>
              </div>
            </div>

            <ul className="pp-feat-modern">
              <li><strong>Rate per Minute:</strong> $0.05</li>
              <li><strong>E-bike Surcharge:</strong> $0</li>
              <li><strong>Free reservations:</strong> docks and bikes included</li>
              <li className="muted">Billed monthly to your account</li>
            </ul>

            <div className="pp-card-actions">
              <span className="pp-cta-modern">
                {selectedPlan === "Premium" ? "Subscribed" : "Select"}
              </span>
            </div>

            {selectedPlan === "Premium" && (
              <div className="pp-selected-badge">Selected</div>
            )}
          </button>
        </div>

        {loading && (
          <div className="pp-loading-modern" role="status" aria-live="polite">
            <svg className="pp-loader" viewBox="0 0 50 50" aria-hidden>
              <circle cx="25" cy="25" r="20" fill="none" strokeWidth="4" />
            </svg>
            <span>Saving your plan...</span>
          </div>
        )}

        {/* Display message inline instead of using alert() */}
        {message && (
  <div
    className={`pp-toast ${
      message.includes("Please log in") ||
      message.includes("cannot") ||
      message.includes("wrong") ||
      message.includes("already")
        ? "pp-toast-error"
        : ""
    }`}
  >
    {message}
  </div>
)}

      </div>
    </section>
  );
}
