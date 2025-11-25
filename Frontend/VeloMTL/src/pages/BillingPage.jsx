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
      <div className="billing-container">
        <div className="billing-header">
          <h1 className="billing-title">Billing History</h1>
          <p className="billing-subtitle">View your past invoices and payments</p>
        </div>
        
        {loading ? (
          <div className="loading">
            <div className="loading-spinner"></div>
            Loading billing history...
          </div>
        ) : billings.length === 0 ? (
          <p className="no-billings">No billing records available.</p>
        ) : (
          <>
            <div className="billing-list">
              {currentBillings.map((bill, index) => (
                <div key={bill.billID} style={{ animationDelay: `${index * 0.1}s` }}>
                  <BillingCard bill={bill} />
                </div>
              ))}
            </div>
            
            {/* Pagination Controls */}
            {totalPages > 1 && (
              <div className="billing-pagination">
                <button
                  className="billing-pagination-button"
                  onClick={handlePreviousPage}
                  disabled={currentPage === 1}
                >
                  ← Previous
                </button>
                
                <span className="billing-pagination-info">
                  Page {currentPage} of {totalPages}
                </span>
                
                <button
                  className="billing-pagination-button"
                  onClick={handleNextPage}
                  disabled={currentPage === totalPages}
                >
                  Next →
                </button>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
}
