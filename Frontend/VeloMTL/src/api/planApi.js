import api from "./api"; 

export const addPlan = async (email, plan) => {
  try {
    const response = await api.post(`/api/riders/add-plan`, {
      email,
      plan,
    });
    return response.data;
  } catch (error) {
    console.error("Error adding plan:", error);
    throw error;
  }
};
