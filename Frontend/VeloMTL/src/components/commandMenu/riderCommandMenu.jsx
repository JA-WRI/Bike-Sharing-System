// src/components/CommandMenu/RiderCommandMenu.jsx
import React, { useContext } from "react";
import { AuthContext } from "../../context/AuthContext";
// import rider API commands later

const RiderCommandMenu = ({ station, dock }) => {
  const { user } = useContext(AuthContext);

  const handleCommand = (cmd) => {
    console.log(`Rider command "${cmd}" sent for dock ${dock.dockId}`);
    // TODO: hook up with riderApi.js later
  };

  return (
    <>
      <div className="command-section">
        <h3>Dock: {dock.dockId}</h3>
        <div className="command-buttons">
          <button onClick={() => handleCommand("reserve-dock")} className="command-btn primary">
            Reserve Dock
          </button>
        </div>
      </div>

      <div className="command-section">
        <h3>Bike: {dock.bikeId}</h3>
        <div className="command-buttons">
          <button onClick={() => handleCommand("reserve-bike")} className="command-btn primary">
            Reserve Bike
          </button>
          <button onClick={() => handleCommand("unlock-bike")} className="command-btn success">
            Unlock Bike
          </button>
          <button onClick={() => handleCommand("lock-bike")} className="command-btn secondary">
            Lock Bike
          </button>
        </div>
      </div>
    </>
  );
};

export default RiderCommandMenu;
