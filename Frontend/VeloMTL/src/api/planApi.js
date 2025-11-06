import api from "./api"; 


export const addPlan = async (riderEmail, chosenPlan) => {
  try {
    const response = await api.post(`api/riders/add-plan`, {
      riderEmail,
      chosenPlan,
    });
    return response.data;
  } catch (error) {
    console.error("Error adding plan:", error);
    throw error;
  }
};
