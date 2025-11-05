import api from "./api";

// Public: anyone can view stations
export const getStationById = async (stationId) => {
  const response = await api.get(`/stations/${stationId}`);
  const stationDTO = response.data;

  return stationDTO;
};