import React from "react";

const SidePanel = ({ station, onClose, loading }) => {
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
            {station.capacity && <p><strong>Capacity:</strong> {station.capacity}</p>}
            {station.stationStatus && <p><strong>Status:</strong> {station.stationStatus}</p>}
            {station.docks?.length > 0 && (
              <>
                <p><strong>Docks:</strong></p>
                <ul>
                  {station.docks.map((dock) => (
                    <li key={dock}>{dock}</li>
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
