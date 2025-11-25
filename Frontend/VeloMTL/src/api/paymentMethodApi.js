import api from "./api";

const PAYMENT_METHOD_BASE_URL = "/api/payment-methods";

export const createSetupIntent = async (email) => {
  try {
    console.log("Calling backend /payment-methods/add with email:", email);
    const response = await api.post(`${PAYMENT_METHOD_BASE_URL}/add`, { email });
    console.log("Backend response:", response.data);
    return response.data;
  } catch (error) {
    // log full details
    if (error.response) {
      console.error("Backend returned error:", error.response.status, error.response.data);
    } else {
      console.error("Error creating SetupIntent:", error);
    }
    throw error;
  }
};

export const checkPaymentMethod = async (email) => {
  try {
    const response = await api.post(`${PAYMENT_METHOD_BASE_URL}/check`, { email });
    return response.data.hasPaymentMethod;
  } catch (error) {
    console.error("Error checking payment method:", error);
    throw error;
  }
};

export const checkStripeCustomerId = async (email) => {
  try {
    const response = await api.post(`${PAYMENT_METHOD_BASE_URL}/check-customer-id`, { email });
    return response.data.hasStripeCustomerId;
  } catch (error) {
    console.error("Error checking Stripe customer ID:", error);
    throw error;
  }
};

export const getPaymentMethods = async (email) => {
  try {
    const response = await api.post(`${PAYMENT_METHOD_BASE_URL}/list`, { email });
    return response.data;
  } catch (error) {
    console.error("Error getting payment methods:", error);
    throw error;
  }
};