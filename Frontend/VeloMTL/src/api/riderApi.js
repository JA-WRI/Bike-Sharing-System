import api from "./api";
import { getBikeById } from "./bikeApi";

export const sendCommand = async (command, { userId, objectId, dockId = null, reserveTime=null }) => {
  const commandDTO = {
    command,
    userId,
    objectId,
    dockId,
    reserveTime
  };

  try {
    const response = await api.post("/api/riders/command", commandDTO);
    return response.data; // expected to be ResponseDTO<?>
  } catch (error) {
    console.error(`Error executing ${command}:`, error);
    throw error;
  }
};

export const reserveBike = (userId, bikeId, dockId, reserveTime) =>
  sendCommand("RB", { userId, objectId: bikeId, dockId, reserveTime });

export const reserveDock = (userId, bikeId, dockId, reserveTime) =>
  sendCommand("RD", { userId, objectId: bikeId, dockId, reserveTime });

export const unlockBike = (userId, bikeId, dockId) => {
  return getBikeById(bikeId).then(bike => {
    if (bike.bikeStatus === "RESERVED" && userId !== bike.reserveUser) {
      console.log("here");
      console.log(userId);
      return { message: "This bike is reserved by another user.", status: "ERROR"};
    }
    return sendCommand("UB", { userId, objectId: bikeId, dockId });
  });
}

export const lockBike = (userId, bikeId, dockId) =>
  sendCommand("LB", { userId, objectId: bikeId, dockId });

export const getTierByEmail = async (email) => {
  const response = await api.get(`/api/tiers/${email}`);
  return response.data;
} 