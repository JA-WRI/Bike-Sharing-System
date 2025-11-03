import React, { useContext } from "react";
import "../styles/CommandMenu.css";
import { AuthContext } from "../context/AuthContext";

const CommandMenu = ({ station, dock, onClose }) => {
  const { user } = useContext(AuthContext);
  if (!dock || !user) return null;

  const handleCommand = (cmd) => {
    console.log(`Command "${cmd}" sent for dock ${dock.dockId}`);
    // TODO: Connect API calls
  };

  const isOperator = user.role === "OPERATOR";

  return (
    <div className="command-menu-overlay">
      <div className="command-menu-modal">
        <div className="command-menu-header">
          <h2 className="command-title">{station.stationName || "Station"}</h2>
          <button className="command-close-btn" onClick={onClose}>
            ✕
          </button>
        </div>

        {isOperator ? (

            //operator command menu
          <>
          
            <div className="command-section">
              <h3>Station: {station.id}</h3>
              <div className="command-buttons">
                <button onClick={() => handleCommand("mark-station-out")} className="command-btn warning">
                  Mark Station Out of Service
                </button>
                <button onClick={() => handleCommand("restore-station")} className="command-btn success">
                  Restore Station
                </button>
              </div>
            </div>

            <div className="command-section">
              <h3>Dock: {dock.dockId}</h3>
              <div className="command-buttons">
                <button onClick={() => handleCommand("mark-docks-out")} className="command-btn warning">
                  Mark Docks Out of Service
                </button>
                <button onClick={() => handleCommand("restore-docks")} className="command-btn success">
                  Restore Docks
                </button>
              </div>
            </div>

            <div className="command-section">
              <h3>Bike: {dock.bikeId}</h3>
              <div className="command-buttons">
                <button onClick={() => handleCommand("unlock-bike")} className="command-btn primary">
                  Unlock Bike
                </button>
                <button onClick={() => handleCommand("lock-bike")} className="command-btn secondary">
                  Lock Bike
                </button>
              </div>
            </div>
          </>
        ) : (
            
            //rider command menu
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
        )}
      </div>
    </div>
  );
};

export default CommandMenu;
