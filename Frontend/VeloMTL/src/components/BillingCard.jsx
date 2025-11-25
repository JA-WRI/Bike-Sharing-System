
import React from "react";
import "../styles/BillingCard.css";

export default function BillingCard({ bill }) {
  const {
    description,
    dateTransaction,
    bikeID,
    originStation,
    arrivalStation,
    startDate,
    endDate,
    ratePerMinute,
    cost,
  } = bill;

  const formattedDate = new Date(dateTransaction).toLocaleString();

  const isTrip = description === "Trip";
  const isMonthly = description === "Monthly Base Fee";

  return (
    <div className={`billing-card ${isTrip ? "trip-card" : "monthly-card"}`}>
      <div className="billing-card-header">
        <h3>{isTrip ? "Trip Billing" : "Monthly Base Fee"}</h3>
        <p className="billing-date">{formattedDate}</p>
      </div>

      <div className="billing-card-body">
        {isTrip ? (
          <>
            <p><strong>Bike ID:</strong> {bikeID || "N/A"}</p>
            <p><strong>Origin Station:</strong> {originStation || "N/A"}</p>
            <p><strong>Arrival Station:</strong> {arrivalStation || "N/A"}</p>
            <p><strong>Start Time:</strong> {startDate ? new Date(startDate).toLocaleString() : "N/A"}</p>
            <p><strong>End Time:</strong> {endDate ? new Date(endDate).toLocaleString() : "N/A"}</p>
            <p><strong>Rate per Minute:</strong> ${ratePerMinute?.toFixed(2) ?? "N/A"}</p>
          </>
        ) : (
          <>
            <p><strong>Billing Type:</strong> Monthly Base Fee</p>
            <p>This charge covers your monthly plan subscription.</p>
          </>
        )}

        <p className="billing-cost">
          <strong>Total Cost:</strong> ${cost.toFixed(2)}
        </p>
      </div>
    </div>
  );
}
