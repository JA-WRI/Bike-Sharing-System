import api from "./api";

// Fetch all billings for a rider
export const getRiderBillings = async (email) => {
  try {
    const response = await api.post("/api/riders/billing", { email });
    return response.data;
  } catch (error) {
    console.error("Error fetching rider billings:", error);
    throw error;
  }
};
