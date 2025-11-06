export const getRiderBilling = async (email) => {
  try {
    const response = await api.post(`/api/riders/billing`, { email });
    return response.data;
  } catch (error) {
    console.error("Error fetching billing data:", error);
    return [];
  }
};
