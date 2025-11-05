// src/components/CommandMenu/RiderCommandMenu.jsx
import React, { useContext } from "react";
import { AuthContext } from "../../context/AuthContext";
import { reserveDock, reserveBike, unlockBike, lockBike } from "../../api/riderApi";

const RiderCommandMenu = ({ station, dock, setResponseMessage, setResponseStatus }) => {
  const { user } = useContext(AuthContext);

  const handleCommand = async (action, ...args) => {
    let extraParams = {};

    if (action === "LB") {
      const bikeId = prompt("Enter the Bike ID to lock:");
      if (!bikeId) {
        alert("Bike ID is required to lock the bike.");
        return;
      }
      extraParams.bikeId = bikeId;
    }

    console.log(`Command "${action}" sent for dock ${dock.dockId}`);

    try {
      let response;

      switch (action) {
        case "RB":
          response = await reserveBike(user.id, dock.bikeId, dock.dockId, new Date().toISOString());
          break;
        case "RD":
          response = await reserveDock(user.id, dock.bikeId, dock.dockId, new Date().toISOString());
          break;
        case "UB":
          response = await unlockBike(user.id, dock.bikeId, dock.dockId);
          break;
        case "LB":
          response = await lockBike(user.id, extraParams.bikeId, dock.dockId);
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
        <h3>Dock: {dock.dockId}</h3>
        <div className="command-buttons">
          <button onClick={() => handleCommand("RD")} className="command-btn primary">
            Reserve Dock
          </button>
        </div>
      </div>

      <div className="command-section">
        <h3>Bike: {dock.bikeId}</h3>
        <div className="command-buttons">
          <button onClick={() => handleCommand("RB")} className="command-btn primary">
            Reserve Bike
          </button>
          <button onClick={() => handleCommand("UB")} className="command-btn success">
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

export default RiderCommandMenu;
