import api from "./api";

export const getBikeById = async (bikeId) => {
  const response = await api.get(`/bikes/${bikeId}`);
  return response.data;
};