import api from "./api";

export const sendCommand = async (command, { userId, objectId, dockId = null }) => {
  const commandDTO = {
    command,
    userId,
    objectId,
    dockId,
  };

  try {
    const response = await api.post("/api/operators/command", commandDTO);
    return response.data; // expected to be ResponseDTO<?>
  } catch (error) {
    console.error(`Error executing ${command}:`, error);
    throw error;
  }
};

export const markStationOutOfService = (userId, stationId) =>
  sendCommand("MSOS", { userId, objectId: stationId });

export const restoreStation = (userId, stationId) =>
  sendCommand("RS", { userId, objectId: stationId });

export const markDockOutOfService = (userId, stationId, dockId) =>
  sendCommand("MDOS", { userId, objectId: stationId, dockId });

export const restoreDock = (userId, stationId, dockId) =>
  sendCommand("RD", { userId, objectId: stationId, dockId });

export const unlockBike = (userId, bikeId, dockId) =>
  sendCommand("UB", { userId, objectId: bikeId, dockId });

export const lockBike = (userId, bikeId, dockId) =>
  sendCommand("LB", { userId, objectId: bikeId, dockId });