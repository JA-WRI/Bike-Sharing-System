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
      if (!user?.id) return;
      try {
        const data = await getRiderBilling(user.id);

        // Separate Trip bills and Monthly Base Fee bills
        const tripBills = data.filter(
          (bill) => bill.description?.toLowerCase() === "trip"
        );
        const monthlyBills = data.filter(
          (bill) => bill.description?.toLowerCase() === "monthly base fee"
        );

        // Merge them: Monthly first, trips later (optional)
        const allBills = [...monthlyBills, ...tripBills];

        setBills(allBills);
      } catch (error) {
        console.error("Error fetching billing:", error);
        setBills([]);
      } finally {
        setLoading(false);
      }
    };

    fetchBills();
  }, [user]);

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
