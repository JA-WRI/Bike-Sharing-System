import api from "./api";

const BILLING_BASE_URL = "/api/billing";

// Fetch all billings for a rider
export const getRiderBillings = async (email) => {
  try {
    const response = await api.post(`${BILLING_BASE_URL}/list`, { email });
    return response.data;
  } catch (error) {
    console.error("Error fetching rider billings:", error);
    throw error;
  }
};
