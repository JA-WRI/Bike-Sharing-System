// src/api/billingApi.js
import api from "./api";

// Get all billing records for a rider
export const getRiderBilling = async (riderId) => {
  try {
    const response = await api.get(`/api/riders/${riderId}/billing`);
    return response.data;
  } catch (error) {
    console.error("Error fetching billing data:", error);
    return [];
  }
};
