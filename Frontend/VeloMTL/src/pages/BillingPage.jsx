import React, { useContext, useEffect, useState } from "react";
import { AuthContext } from "../context/AuthContext";
import { getRiderBillings } from "../api/billingApi";
import BillingCard from "../components/BillingCard";
import "../styles/BillingPage.css";

export default function BillingPage() {
  const { user } = useContext(AuthContext);
  const [billings, setBillings] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchBillings = async () => {
      try {
        const data = await getRiderBillings(user.email);
        setBillings(data);
      } catch (error) {
        console.error("Failed to fetch billings:", error);
      } finally {
        setLoading(false);
      }
    };

    if (user?.email) fetchBillings();
  }, [user]);

  if (loading) return <p className="loading">Loading billing history...</p>;

  return (
    <div className="billing-page">
      <h1 className="billing-title">Billings</h1>
      {billings.length === 0 ? (
        <p className="no-billings">No billing records available.</p>
      ) : (
        <div className="billing-list">
          {billings.map((bill) => (
            <BillingCard key={bill.billID} bill={bill} />
          ))}
        </div>
      )}
    </div>
  );
}
