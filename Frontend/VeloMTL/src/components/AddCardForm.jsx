import React, { useState, useEffect } from "react";
import { CardElement, useStripe, useElements } from "@stripe/react-stripe-js";
import axios from "axios";

export default function AddCardForm({ riderEmail }) {
  const stripe = useStripe();
  const elements = useElements();

  const [clientSecret, setClientSecret] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    // Call backend to create Stripe SetupIntent
    if (!riderEmail) return;

    axios
      .post("http://localhost:8080/api/riders/addPaymentMethod", { email: riderEmail })
      .then((res) => {
        // Expecting RiderDTO with clientSecret
        setClientSecret(res.data.clientSecret);
        setMessage(""); // clear any previous error
      })
      .catch((err) => {
        console.error(err);
        setMessage("Failed to initialize payment method");
      });
  }, [riderEmail]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!stripe || !elements || !clientSecret) return;

    const cardElement = elements.getElement(CardElement);

    const { setupIntent, error } = await stripe.confirmCardSetup(clientSecret, {
      payment_method: {
        card: cardElement,
        billing_details: { email: riderEmail },
      },
    });

    if (error) {
      setMessage(error.message);
    } else {
      setMessage(`Card added! Payment method ID: ${setupIntent.payment_method}`);
      console.log("SetupIntent:", setupIntent);
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ maxWidth: "400px", marginTop: "20px" }}>
      <CardElement />
      <button type="submit" disabled={!stripe || !clientSecret} style={{ marginTop: "20px" }}>
        Add Card
      </button>
      {message && <p>{message}</p>}
    </form>
  );
}
