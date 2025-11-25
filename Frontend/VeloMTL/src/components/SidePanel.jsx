import React from "react";

const SidePanel = ({ station, onClose, loading, onDockSelect }) => {
  if (!station) return null; // Don't render panel if no station selected

  const getFullnessColor = () => {
    const capacity = station.capacity ?? 0;
    const occupancy = station.occupancy ?? 0;

    if (capacity <= 0) return "#7f8c8d"; 
    const fullness = (occupancy / capacity) * 100;

    if (fullness === 0 || fullness === 100) return "#e74c3c"; 
    if (fullness < 25 || fullness > 85) return "#f1c40f"; 
    return "#2ecc71"; 
  };

  const fullnessColor = getFullnessColor();

  // Helper: find the bike in this station that is at the given dock
  const findBikeForDock = (dockId) => {
    if (!station.bikes) return null;
    return station.bikes.find((b) => b.dockId === dockId);
  };

  return (
    <div className="side-panel">
      <div className="side-panel-header">
        <h2 className="station-header">
          <span
            className="station-status-dot"
            style={{ backgroundColor: fullnessColor }}
          ></span>
          {station.stationName || "Loading..."}
        </h2>
        <button className="close-btn" onClick={onClose}>
          ✕
        </button>
      </div>

      <div className="side-panel-content">
        {loading ? (
          <p>Loading station data...</p>
        ) : (
          <>
            {station.streetAddress && <p>{station.streetAddress}</p>}
            {station.capacity && (
              <p>
                <strong>Capacity:</strong> {station.capacity}
              </p>
            )}
            {station.stationStatus && (
              <p>
                <strong>Status:</strong> {station.stationStatus}
              </p>
            )}
            {station.occupancy && (
              <p>
                <strong>Occupancy:</strong> {station.occupancy}
              </p>
            )}

            {station.docks?.length > 0 && (
              <>
                <p>
                  <strong>Docks:</strong>
                </p>
                <ul className="dock-list">
                  {station.docks.map((dock) => {
                    const bike = dock.bike || findBikeForDock(dock.dockId);

                    return (
                      <li
                        key={dock.dockId}
                        className="dock-item"
                        onClick={() => onDockSelect(dock)}
                        style={{ cursor: "pointer" }}
                      >
                        <p>
                          <strong>Dock:</strong> {dock.dockId}{" "}
                          <span
                            style={{
                              color: dock.status === "EMPTY" ? "green" : "red",
                            }}
                          >
                            ({dock.status})
                          </span>
                        </p>

                        {bike ? (
                          <div className="bike-item">
                            🚲 <strong>{bike.bikId}</strong>{" "}
                            <span style={{ color: "#555" }}>
                              ({bike.bikeType === "electric" || bike.bikeType === "ELECTRIC" ? "E" : "S"})
                            </span>
                            {bike.bikeStatus === "RESERVED" && (
                              <span style={{ color: "red" }}>
                                &nbsp; (Reserved)
                              </span>
                            )}
                          </div>
                        ) : (
                          <p className="no-bike">No bike in this dock</p>
                        )}
                      </li>
                    );
                  })}
                </ul>
              </>
            )}
          </>
        )}
      </div>
    </div>
  );
};

export default SidePanel;
