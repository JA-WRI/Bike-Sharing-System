import React, { useEffect, useState, useContext } from "react";
import { getRiderBilling } from "../api/billingApi";
import { AuthContext } from "../context/AuthContext";
import BillingCard from "../components/BillingCard";
import "../styles/BillingPage.css";

export default function BillingPage() {
  const { user } = useContext(AuthContext);
  const [bills, setBills] = useState([]);
  const [expandedBill, setExpandedBill] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchBills = async () => {
      try {
        setLoading(true);

        // 🧩 Try both potential ID fields
        const riderID = user?.riderID || user?.id;

        if (!riderID) {
          console.warn("⏳ No rider ID yet, skipping billing fetch.");
          setLoading(false);
          return;
        }

        console.log("📡 Fetching billing for riderID:", riderID);

        const data = await getRiderBilling(user.email);
        console.log("Billing API response:", data);

        const billsArray = Array.isArray(data)
          ? data
          : data?.data || data?.bills || [];

        const tripBills = billsArray.filter(
          (bill) => bill.description?.toLowerCase() === "trip"
        );
        const monthlyBills = billsArray.filter(
          (bill) => bill.description?.toLowerCase() === "monthly base fee"
        );

        setBills([...monthlyBills, ...tripBills]);
      } catch (error) {
        console.error("Error fetching billing:", error);
        setBills([]);
      } finally {
        setLoading(false);
      }
    };

    fetchBills();
  }, [user?.id, user?.riderID]);

  const toggleExpand = (billID) => {
    setExpandedBill((prev) => (prev === billID ? null : billID));
  };

  if (loading) {
    return <p style={{ textAlign: "center" }}>Loading billing records...</p>;
  }

  return (
    <div className="billing-container">
      <h1 className="billing-title">Billing History</h1>

      {bills.length === 0 ? (
        <p style={{ textAlign: "center", color: "#64748b" }}>
          No billing records yet.
        </p>
      ) : (
        <div className="billing-list">
          {bills.map((bill) => (
            <BillingCard
              key={bill.billID}
              bill={bill}
              isExpanded={expandedBill === bill.billID}
              onClick={() => toggleExpand(bill.billID)}
            />
          ))}
        </div>
      )}
    </div>
  );
}
