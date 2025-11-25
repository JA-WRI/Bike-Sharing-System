import React, { useContext, useState, useEffect } from "react";
import AddCardForm from "../components/AddPaymentMethod";
import { AuthContext } from "../context/AuthContext";
import { getPaymentMethods } from "../api/paymentMethodApi";
import "../styles/AddPayment.css";

const AddPayment = () => {
  const { user } = useContext(AuthContext);
  const [paymentMethods, setPaymentMethods] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchPaymentMethods = async () => {
    if (!user) return;
    
    try {
      setLoading(true);
      const methods = await getPaymentMethods(user.email);
      setPaymentMethods(methods || []);
    } catch (error) {
      console.error("Failed to fetch payment methods:", error);
      setPaymentMethods([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPaymentMethods();
  }, [user]);

  if (!user) {
    return (
      <div className="add-payment-page">
        <div className="add-payment-container">
          <p style={{ textAlign: "center", color: "var(--text-muted)", fontSize: "1.1rem" }}>
            Please log in to add a payment method.
          </p>
        </div>
      </div>
    );
  }

  const formatCardBrand = (brand) => {
    if (!brand) return "Card";
    return brand.charAt(0).toUpperCase() + brand.slice(1);
  };

  const formatExpirationDate = (month, year) => {
    if (!month || !year) return "N/A";
    const monthStr = month.toString().padStart(2, "0");
    return `${monthStr}/${year.toString().slice(-2)}`;
  };

  return (
    <div className="add-payment-page">
      <div className="add-payment-container">
        <div className="add-payment-header">
          <h1>Payment Methods</h1>
          <p>Manage your payment cards and billing information</p>
        </div>

        {loading ? (
          <div className="loading-state">
            <div className="loading-spinner"></div>
          </div>
        ) : (
          <>
            {/* Display existing payment methods */}
            {paymentMethods.length > 0 && (
              <div className="payment-cards-section">
                <h2 className="section-title">Your Cards</h2>
                <div className="payment-cards-grid">
                  {paymentMethods.map((card, index) => (
                    <div key={card.id} className="payment-card" style={{ animationDelay: `${index * 0.1}s` }}>
                      <div className="payment-card-header">
                        <div>
                          <div className="cardholder-name">
                            {card.name || "Cardholder Name"}
                          </div>
                          <div className="card-details">
                            <div className="card-number">
                              {formatCardBrand(card.brand)} •••• {card.last4}
                            </div>
                            <div className="card-expiry">
                              Expires {formatExpirationDate(card.expMonth, card.expYear)}
                            </div>
                          </div>
                        </div>
                        <div className="card-status">Active</div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* Add new card form */}
            <div className="add-card-section">
              <h2 className="section-title">
                {paymentMethods.length > 0 ? "Add Another Card" : "Add a Payment Method"}
              </h2>
              <AddCardForm riderEmail={user.email} riderName={user.name} onCardAdded={fetchPaymentMethods} />
            </div>

            {paymentMethods.length === 0 && !loading && (
              <div className="empty-state">
                <div className="empty-state-icon">💳</div>
                <div className="empty-state-text">No payment methods yet</div>
                <div className="empty-state-subtext">Add a card to get started</div>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
};

export default AddPayment;
