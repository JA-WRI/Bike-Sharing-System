import React from "react";

const SidePanel = ({ station, onClose }) => {
  if (!station) return null; // Only show panel when a station is selected

  return (
    <div className="side-panel">
      <div className="side-panel-header">
        <h2>{station.name}</h2>
        <button className="close-btn" onClick={onClose}>
          ✕
        </button>
      </div>
      <div className="side-panel-content">
        <p>Loading data for <strong>{station.name}</strong>...</p>
        {/* Later: Replace this with dynamic bike/dock info from your API */}
      </div>
    </div>
  );
};

export default SidePanel;
