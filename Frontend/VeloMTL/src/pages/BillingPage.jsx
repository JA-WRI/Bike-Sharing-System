import React, { useContext, useEffect, useState } from "react";
import { AuthContext } from "../context/AuthContext";
import { getRiderBillings } from "../api/billingApi";
import BillingCard from "../components/BillingCard";
import "../styles/BillingPage.css";

const ITEMS_PER_PAGE = 2;

export default function BillingPage() {
  const { user } = useContext(AuthContext);
  const [billings, setBillings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);

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

  // Reset to page 1 when billings change
  useEffect(() => {
    setCurrentPage(1);
  }, [billings]);

  if (loading) return <p className="loading">Loading billing history...</p>;

  // Calculate pagination
  const totalPages = Math.ceil(billings.length / ITEMS_PER_PAGE);
  const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
  const endIndex = startIndex + ITEMS_PER_PAGE;
  const currentBillings = billings.slice(startIndex, endIndex);

  const handleNextPage = () => {
    if (currentPage < totalPages) {
      setCurrentPage(currentPage + 1);
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  };

  const handlePreviousPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  };

  return (
    <div className="billing-page">
      <h1 className="billing-title">Billings</h1>
      {billings.length === 0 ? (
        <p className="no-billings">No billing records available.</p>
      ) : (
        <>
          <div className="billing-list">
            {currentBillings.map((bill) => (
              <BillingCard key={bill.billID} bill={bill} />
            ))}
          </div>
          
          {/* Pagination Controls */}
          {totalPages > 1 && (
            <div style={{
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              gap: "16px",
              marginTop: "32px",
              padding: "20px"
            }}>
              <button
                onClick={handlePreviousPage}
                disabled={currentPage === 1}
                style={{
                  padding: "10px 20px",
                  borderRadius: "6px",
                  border: "1px solid #ddd",
                  backgroundColor: currentPage === 1 ? "#f5f5f5" : "white",
                  color: currentPage === 1 ? "#999" : "#333",
                  cursor: currentPage === 1 ? "not-allowed" : "pointer",
                  fontSize: "14px",
                  fontWeight: "500",
                  display: "flex",
                  alignItems: "center",
                  gap: "8px"
                }}
              >
                ← Previous
              </button>
              
              <span style={{
                fontSize: "14px",
                color: "#666",
                fontWeight: "500"
              }}>
                Page {currentPage} of {totalPages}
              </span>
              
              <button
                onClick={handleNextPage}
                disabled={currentPage === totalPages}
                style={{
                  padding: "10px 20px",
                  borderRadius: "6px",
                  border: "1px solid #ddd",
                  backgroundColor: currentPage === totalPages ? "#f5f5f5" : "white",
                  color: currentPage === totalPages ? "#999" : "#333",
                  cursor: currentPage === totalPages ? "not-allowed" : "pointer",
                  fontSize: "14px",
                  fontWeight: "500",
                  display: "flex",
                  alignItems: "center",
                  gap: "8px"
                }}
              >
                Next →
              </button>
            </div>
          )}
        </>
      )}
    </div>
  );
}
