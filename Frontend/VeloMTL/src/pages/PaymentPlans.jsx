import React, { useContext, useState, useEffect } from "react";
import "../styles/PaymentPlans.css";
import { AuthContext } from "../context/AuthContext";
import { addPlan, getCurrentPlan } from "../api/planApi";
import { checkPaymentMethod, checkStripeCustomerId } from "../api/paymentMethodApi";
import { Link, useLocation } from "react-router-dom";

export default function PaymentPlans() {
  const { user, activeRole } = useContext(AuthContext);
  const location = useLocation();
  const [selectedPlan, setSelectedPlan] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const [hasPaymentMethod, setHasPaymentMethod] = useState(false);
  const [hasStripeCustomerId, setHasStripeCustomerId] = useState(false);
  const [checkingPaymentMethod, setCheckingPaymentMethod] = useState(true);
  const [hasExistingPlan, setHasExistingPlan] = useState(false);
  const [showConfirmation, setShowConfirmation] = useState(false);
  const [planToConfirm, setPlanToConfirm] = useState(null);

  useEffect(() => {
    const checkPaymentAndPlan = async () => {
      if (!user) {
        setCheckingPaymentMethod(false);
        return;
      }

      try {
        setCheckingPaymentMethod(true);
        // Check Stripe customer ID, payment method, and existing plan in parallel
        // Using user.email ensures operators and riders share the same payment data
        // Note: Operators and riders use the same endpoints (based on email),
        // so payment methods/plans added as an operator are available in rider view
        // We check payment methods for operators too, so when they switch to rider view,
        // the state is already correct
        const [hasStripeId, hasPayment, currentPlan] = await Promise.all([
          checkStripeCustomerId(user.email),
          checkPaymentMethod(user.email),
          getCurrentPlan(user.email)
        ]);
        setHasStripeCustomerId(hasStripeId);
        setHasPaymentMethod(hasPayment);
        if (currentPlan) {
          setSelectedPlan(currentPlan);
          setHasExistingPlan(true);
        } else {
          setHasExistingPlan(false);
        }
      } catch (error) {
        console.error("Failed to check payment method or plan:", error);
        setHasStripeCustomerId(false);
        setHasPaymentMethod(false);
      } finally {
        setCheckingPaymentMethod(false);
      }
    };

    checkPaymentAndPlan();
  }, [user, activeRole, location.pathname]);

  const handleSelectPlan = (plan) => {
    //Check login before allowing plan selection
    if (!user) {
      setMessage("Please log in to select a payment plan.");
      return;
    }

    if (activeRole === "OPERATOR") {
      setMessage("Operators cannot select a payment plan. Switch to rider view to select a plan.");
      return;
    }

    // Check if user has a Stripe customer ID (which means they've started adding a payment method)
    if (!hasStripeCustomerId) {
      setMessage("Please add a payment method before selecting a plan.");
      return;
    }

    // Check if user has a payment method
    if (!hasPaymentMethod) {
      setMessage("Please add a payment method before selecting a plan.");
      return;
    }

    if (selectedPlan) {
      setMessage("You already have a plan. You can only change it next year.");
      return;
    }

    // Show confirmation dialog
    setPlanToConfirm(plan);
    setShowConfirmation(true);
  };

  const handleConfirmPlan = async () => {
    if (!planToConfirm) return;

    try {
      setLoading(true);
      setShowConfirmation(false);
      const res = await addPlan(user.email, planToConfirm);
      console.log(res);
      setSelectedPlan(planToConfirm);
      setMessage(`Thank you! Your ${planToConfirm} membership was added to your account.`);
      setPlanToConfirm(null);
    } catch (error) {
      console.error("Failed to add plan:", error);
      // Show backend error message if available
      const errorMessage = error.response?.data || error.message || "Something went wrong while adding your plan. Please try again.";
      setMessage(typeof errorMessage === 'string' ? errorMessage : "Something went wrong while adding your plan. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const handleCancelConfirmation = () => {
    setShowConfirmation(false);
    setPlanToConfirm(null);
  };

  const isDisabled = !user || activeRole === "OPERATOR" || !hasStripeCustomerId || !hasPaymentMethod || checkingPaymentMethod;

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

        {!user && (
          <div className="pp-note-modern" style={{ 
            backgroundColor: "#fee", 
            border: "1px solid #f44336", 
            padding: "16px", 
            borderRadius: "8px",
            marginBottom: "24px"
          }}>
            <p style={{ margin: "0 0 8px 0", fontWeight: "600", color: "#c62828" }}>
              Login Required
            </p>
            <p style={{ margin: 0, fontSize: "14px", color: "#d32f2f" }}>
              You need to <Link to="/login" style={{ color: "#0066cc", textDecoration: "underline", fontWeight: "600" }}>log in</Link> to select a payment plan.
            </p>
          </div>
        )}

        {activeRole === "OPERATOR" && user?.role === "OPERATOR" && (
          <div className="pp-note-modern" style={{ 
            backgroundColor: "#e3f2fd", 
            border: "1px solid #2196f3", 
            padding: "16px", 
            borderRadius: "8px",
            marginBottom: "24px"
          }}>
            <p style={{ margin: "0 0 8px 0", fontWeight: "600" }}>
              View Only Mode
            </p>
            <p style={{ margin: 0, fontSize: "14px" }}>
              You are viewing this page as an operator. Switch to rider view to select a payment plan.
            </p>
          </div>
        )}

        {user && activeRole !== "OPERATOR" && !checkingPaymentMethod && (!hasStripeCustomerId || !hasPaymentMethod) && !hasExistingPlan && (
          <div className="pp-note-modern" style={{ 
            backgroundColor: "#fff3cd", 
            border: "1px solid #ffc107", 
            padding: "16px", 
            borderRadius: "8px",
            marginBottom: "24px"
          }}>
            <p style={{ margin: "0 0 8px 0", fontWeight: "600" }}>
              Payment Method Required
            </p>
            <p style={{ margin: 0, fontSize: "14px" }}>
              You need to add a payment method before selecting a plan.{" "}
              <Link to="/add-payment" style={{ color: "#0066cc", textDecoration: "underline" }}>
                Add Payment Method
              </Link>
            </p>
          </div>
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
              <li><strong>Rate per Minute:</strong> $0.20</li>
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
              <li><strong>Rate per Minute:</strong> $0.15</li>
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
      message.includes("already") ||
      message.includes("Please add a payment method")
        ? "pp-toast-error"
        : ""
    }`}
  >
    {message}
  </div>
)}

        {/* Confirmation Modal */}
        {showConfirmation && planToConfirm && (
          <div 
            className="pp-modal-overlay"
            style={{
              position: "fixed",
              top: 0,
              left: 0,
              right: 0,
              bottom: 0,
              backgroundColor: "rgba(0, 0, 0, 0.5)",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              zIndex: 1000,
            }}
            onClick={handleCancelConfirmation}
          >
            <div
              className="pp-modal-content"
              style={{
                backgroundColor: "white",
                borderRadius: "12px",
                padding: "32px",
                maxWidth: "500px",
                width: "90%",
                boxShadow: "0 10px 40px rgba(0, 0, 0, 0.2)",
              }}
              onClick={(e) => e.stopPropagation()}
            >
              <h2 style={{ marginTop: 0, marginBottom: "16px", fontSize: "24px", fontWeight: "600" }}>
                Confirm Payment Plan Selection
              </h2>
              <p style={{ marginBottom: "24px", color: "#666", lineHeight: "1.6" }}>
                Are you sure you want to select the <strong>{planToConfirm}</strong> plan? 
                This plan will be billed monthly to your payment method.
              </p>
              <div style={{ display: "flex", gap: "12px", justifyContent: "flex-end" }}>
                <button
                  onClick={handleCancelConfirmation}
                  style={{
                    padding: "10px 24px",
                    borderRadius: "6px",
                    border: "1px solid #ddd",
                    backgroundColor: "white",
                    cursor: "pointer",
                    fontSize: "14px",
                    fontWeight: "500",
                    color: "#333",
                  }}
                >
                  Cancel
                </button>
                <button
                  onClick={handleConfirmPlan}
                  disabled={loading}
                  style={{
                    padding: "10px 24px",
                    borderRadius: "6px",
                    border: "none",
                    backgroundColor: "#0066cc",
                    color: "white",
                    cursor: loading ? "not-allowed" : "pointer",
                    fontSize: "14px",
                    fontWeight: "500",
                    opacity: loading ? 0.6 : 1,
                  }}
                >
                  {loading ? "Processing..." : "Confirm"}
                </button>
              </div>
            </div>
          </div>
        )}

      </div>
    </section>
  );
}
