import React, { useContext } from "react";
import { AuthContext } from "../../context/AuthContext";
import {
  markStationOutOfService,
  restoreStation,
  markDockOutOfService,
  restoreDock,
  unlockBike,
  lockBike,
} from "../../api/operatorApi";

const OperatorCommandMenu = ({ station, dock, setResponseMessage, setResponseStatus }) => {
  const { user } = useContext(AuthContext);

  const handleCommand = async (action, ...args) => {
    try {
      let response;

      switch (action) {
        case "MSOS":
          response = await markStationOutOfService(user.id, station.id);
          break;
        case "RS":
          response = await restoreStation(user.id, station.id);
          break;
        case "MDOS":
          response = await markDockOutOfService(user.id, station.id, dock.dockId);
          break;
        case "RD":
          response = await restoreDock(user.id, station.id, dock.dockId);
          break;
        case "UB":
          response = await unlockBike(user.id, dock.bikeId, dock.dockId);
          break;
        case "LB":
          response = await lockBike(user.id, dock.bikeId, dock.dockId);
          break;
        default:
          console.warn("Unknown operator command:", action);
      }

      console.log("Command response:", response);
      setResponseMessage(response.message);
      setResponseStatus(response.status);
    } catch (err) {
      console.error("Command failed:", err);
      setResponseMessage("An error occurred while performing the command.");
      setResponseStatus("ERROR");
    }
  };

  return (
    <>
      <div className="command-section">
        <h3>Station: {station.id}</h3>
        <div className="command-buttons">
          <button onClick={() => handleCommand("MSOS")} className="command-btn warning">
            Mark Station Out of Service
          </button>
          <button onClick={() => handleCommand("RS")} className="command-btn success">
            Restore Station
          </button>
        </div>
      </div>

      <div className="command-section">
        <h3>Dock: {dock.dockId}</h3>
        <div className="command-buttons">
          <button onClick={() => handleCommand("MDOS")} className="command-btn warning">
            Mark Dock Out of Service
          </button>
          <button onClick={() => handleCommand("RD")} className="command-btn success">
            Restore Dock
          </button>
        </div>
      </div>

      <div className="command-section">
        <h3>Bike: {dock.bikeId}</h3>
        <div className="command-buttons">
          <button onClick={() => handleCommand("UB")} className="command-btn primary">
            Unlock Bike
          </button>
          <button onClick={() => handleCommand("LB")} className="command-btn secondary">
            Lock Bike
          </button>
        </div>
      </div>
    </>
  );
};

export default OperatorCommandMenu;
