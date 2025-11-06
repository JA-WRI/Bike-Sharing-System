// src/api/billingApi.js
import api from "./api";

// Get all billing records for a rider
export const getRiderBilling = async (riderID) => {
  try {
    const response = await api.get(`/api/riders/${riderID}/billing`);
    return response.data;
  } catch (error) {
    console.error("Error fetching billing data:", error);
    return [];
  }
};
