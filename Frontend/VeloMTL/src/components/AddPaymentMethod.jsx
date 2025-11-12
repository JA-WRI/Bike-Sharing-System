import React, { useState, useEffect, useRef } from "react";
import { CardElement, useStripe, useElements } from "@stripe/react-stripe-js";
import { createSetupIntent } from "../api/paymentMethodApi";

export default function AddCardForm({ riderEmail, riderName, onCardAdded }) {
  const stripe = useStripe();
  const elements = useElements();

  const [clientSecret, setClientSecret] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  // Ref to prevent multiple simultaneous backend calls
  const isInitializing = useRef(false);

  useEffect(() => {
    if (!riderEmail) return;
    if (clientSecret || isInitializing.current) return; // already initialized

    const initPayment = async () => {
      isInitializing.current = true; // lock
      setLoading(true);
      try {
        const data = await createSetupIntent(riderEmail);
        console.log("SetupIntent response from backend:", data);

        if (!data?.clientSecret) {
          throw new Error("No clientSecret returned from backend");
        }

        setClientSecret(data.clientSecret);
        setMessage("");
      } catch (err) {
        console.error("Failed to initialize payment method:", err);
        setMessage("Failed to initialize payment method. Please try again.");
      } finally {
        setLoading(false);
        isInitializing.current = false; // release lock
      }
    };

    initPayment();
  }, [riderEmail, clientSecret]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!stripe || !elements || !clientSecret) return;

    setLoading(true);
    setMessage("");

    const cardElement = elements.getElement(CardElement);

    if (!cardElement) {
      setMessage("Card input not loaded. Try again.");
      setLoading(false);
      return;
    }

    try {
      const result = await stripe.confirmCardSetup(clientSecret, {
        payment_method: {
          card: cardElement,
          billing_details: { 
            email: riderEmail,
            name: riderName || undefined
          },
        },
      });

      if (result.error) {
        console.error("Error confirming card setup:", result.error);
        setMessage(result.error.message);
      } else {
        setMessage("Card added successfully!");
        console.log("SetupIntent:", result.setupIntent);
        
        // Clear the form
        if (cardElement) {
          cardElement.clear();
        }
        
        // Notify parent component to refresh payment methods list
        if (onCardAdded) {
          // Small delay to ensure Stripe has processed the payment method
          setTimeout(() => {
            onCardAdded();
          }, 500);
        }
      }
    } catch (err) {
      console.error("Unexpected error during payment setup:", err);
      setMessage("Unexpected error during payment setup.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ maxWidth: "400px", marginTop: "20px" }}>
      <CardElement
        options={{
          hidePostalCode: true,
          style: {
            base: {
              fontSize: "16px",
              color: "#32325d",
              "::placeholder": { color: "#a0aec0" },
            },
            invalid: {
              color: "#fa755a",
            },
          },
        }}
      />
      <button
        type="submit"
        disabled={!stripe || !clientSecret || loading}
        style={{ marginTop: "20px", opacity: !stripe || !clientSecret || loading ? 0.5 : 1 }}
      >
        {loading ? "Processing..." : "Add Card"}
      </button>
      {message && (
        <p
          style={{
            marginTop: "10px",
            color: message.includes("successfully") ? "green" : "red",
          }}
        >
          {message}
        </p>
      )}
    </form>
  );
}
