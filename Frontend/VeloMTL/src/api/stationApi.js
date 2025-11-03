import api from "./api";

// Public: can view stations
export const getStationById = async (stationId) => {
  const response = await api.get(`/stations/${stationId}`);
  return response.data;
};