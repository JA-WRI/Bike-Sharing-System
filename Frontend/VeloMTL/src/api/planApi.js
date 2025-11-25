import api from "./api"; 

const PAYMENT_PLAN_BASE_URL = "/api/payment-plans";

export const addPlan = async (email, plan) => {
  try {
    const response = await api.post(`${PAYMENT_PLAN_BASE_URL}/add`, {
      email,
      plan,
    });
    return response.data;
  } catch (error) {
    console.error("Error adding plan:", error);
    throw error;
  }
};

export const getCurrentPlan = async (email) => {
  try {
    const response = await api.post(`${PAYMENT_PLAN_BASE_URL}/get`, {
      email,
    });
    return response.data.plan; // Returns "Basic", "Premium", or null
  } catch (error) {
    console.error("Error getting plan:", error);
    throw error;
  }
};