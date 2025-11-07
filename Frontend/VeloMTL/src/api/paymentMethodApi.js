import api from "./api";

const RIDER_BASE_URL = "/api/riders";

export const createSetupIntent = async (email) => {
  try {
    console.log("Calling backend /addPaymentMethod with email:", email);
    const response = await api.post(`${RIDER_BASE_URL}/addPaymentMethod`, { email });
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
