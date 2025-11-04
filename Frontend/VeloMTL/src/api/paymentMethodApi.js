import api from "./api";

const RIDER_BASE_URL = "/api/riders";

export const createSetupIntent = async (email) => {
  try {
    const response = await api.post(`${RIDER_BASE_URL}/addPaymentMethod`, { email });
    return response.data;
  } catch (error) {
    console.error("Error creating SetupIntent:", error);
    throw error;
  }
};