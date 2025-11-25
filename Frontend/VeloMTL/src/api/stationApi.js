import api from "./api";

// Public: anyone can view stations
export const getStationById = async (stationId) => {
  const response = await api.get(`/stations/${stationId}`);
  return response.data;
};

// Get all bikes associated with a station (e.g. for showing ebike vs standard)
export const getBikesByStationId = async (stationId) => {
  const response = await api.get(`/stations/${stationId}/bikes`);
  return response.data;
};