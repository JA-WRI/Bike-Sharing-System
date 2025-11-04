import React, { useContext } from "react";
import AddCardForm from "../components/AddCardForm";
import { AuthContext } from "../context/AuthContext";

const AddPayment = () => {
  const { user } = useContext(AuthContext);

  if (!user) return <p>Please log in to add a payment method.</p>;

  return (
    <div style={{ padding: "20px" }}>
      <h1>Add a Payment Method</h1>
      <AddCardForm riderEmail={user.email} />
    </div>
  );
};

export default AddPayment;
