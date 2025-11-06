import React from "react";
import "../styles/BillingPage.css";

export default function BillingCard({ bill, isExpanded, onClick }) {
  const isTrip = bill.description?.toLowerCase() === "trip";
  const trip = bill.trip || {};

  return (
    <div
      className={`billing-card ${isExpanded ? "expanded" : ""}`}
      onClick={onClick}
    >
      <p><strong>Bill ID:</strong> {bill.billID || "N/A"}</p>
      <p><strong>Rider ID:</strong> {bill.riderID || "N/A"}</p>
      <p><strong>Description:</strong> {bill.description || "N/A"}</p>

      {isExpanded && (
        <div className="billing-details">
          {isTrip ? (
            <>
              <p><strong>Bike ID:</strong> {trip?.bike?.bikeId || "N/A"}</p>
              <p><strong>Start Time:</strong> {trip?.startTime ? new Date(trip.startTime).toLocaleString() : "N/A"}</p>
              <p><strong>End Time:</strong> {trip?.endTime ? new Date(trip.endTime).toLocaleString() : "N/A"}</p>
              <p><strong>Origin Station:</strong> {trip?.originStation || "N/A"}</p>
              <p><strong>Arrival Station:</strong> {trip?.arrivalStation || "N/A"}</p>
              <p><strong>Plan Rate:</strong> {trip?.rate != null ? `$${trip.rate.toFixed(2)}` : "N/A"}</p>
              <p><strong>E-Bike Surcharge:</strong> {trip?.isEBike ? "$5.00" : "$0.00"}</p>
            </>
          ) : (
            <p><strong>Date:</strong> {bill?.dateTransaction ? new Date(bill.dateTransaction).toLocaleString() : "N/A"}</p>
          )}
          <p><strong>Total Cost:</strong> ${bill?.cost?.toFixed(2) || "0.00"}</p>
        </div>
      )}
    </div>
  );
}
