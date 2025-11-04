import React from "react";


const SidePanel = ({ station, onClose, loading, onDockSelect }) => {
  if (!station) return null; // Don't render panel if no station selected

  return (
    <div className="side-panel">
      <div className="side-panel-header">
        <h2>{station.stationName || "Loading..."}</h2>
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
                  {station.docks.map((dock) => (
                    <li 
                    key={dock.dockId} 
                    className="dock-item"
                    onClick={() => onDockSelect(dock)}
                    style={{ cursor: "pointer" }}
                    >
                      <p>
                        <strong>Dock:</strong> {dock.dockId}{" "}
                        <span style={{ color: dock.status === "EMPTY" ? "green" : "red" }}>
                          ({dock.status})
                        </span>
                      </p>

                      {dock.bikeId ? (
                        <div className="bike-item">
                          🚲 <strong>{dock.bikeId}</strong>
                        </div>
                      ) : (
                        <p className="no-bike">No bike in this dock</p>
                      )}
                    </li>
                  ))}
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
