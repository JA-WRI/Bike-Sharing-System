import React, { useContext, useState, useEffect } from "react";
import AddCardForm from "../components/AddPaymentMethod";
import { AuthContext } from "../context/AuthContext";
import { getPaymentMethods } from "../api/paymentMethodApi";

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

  if (!user) return <p>Please log in to add a payment method.</p>;

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
    <div style={{ padding: "20px", maxWidth: "800px" }}>
      <h1>Payment Methods</h1>
      
      {/* Display existing payment methods */}
      {paymentMethods.length > 0 && (
        <div style={{ marginBottom: "30px" }}>
          <h2 style={{ marginBottom: "15px", fontSize: "18px" }}>Your Cards</h2>
          <div style={{ display: "flex", flexDirection: "column", gap: "15px" }}>
            {paymentMethods.map((card) => (
              <div
                key={card.id}
                style={{
                  border: "1px solid #e0e0e0",
                  borderRadius: "8px",
                  padding: "20px",
                  backgroundColor: "#f9f9f9",
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                }}
              >
                <div style={{ display: "flex", flexDirection: "column", gap: "5px" }}>
                  <div style={{ fontWeight: "600", fontSize: "16px" }}>
                    {card.name || "Cardholder Name"}
                  </div>
                  <div style={{ color: "#666", fontSize: "14px" }}>
                    {formatCardBrand(card.brand)} •••• {card.last4}
                  </div>
                  <div style={{ color: "#666", fontSize: "14px" }}>
                    Expires {formatExpirationDate(card.expMonth, card.expYear)}
                  </div>
                </div>
                <div style={{ color: "#4caf50", fontWeight: "500" }}>
                  ✓ Active
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Add new card form */}
      <div>
        <h2 style={{ marginBottom: "15px", fontSize: "18px" }}>
          {paymentMethods.length > 0 ? "Add Another Card" : "Add a Payment Method"}
        </h2>
        <AddCardForm riderEmail={user.email} riderName={user.name} onCardAdded={fetchPaymentMethods} />
      </div>
    </div>
  );
};

export default AddPayment;
