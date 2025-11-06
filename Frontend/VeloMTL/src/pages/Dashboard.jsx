import React, { useState, useEffect, useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import MapView from "../components/MapView";
import SidePanel from "../components/SidePanel";
import '../styles/Dashboard.css';
import '../styles/SidePanel.css';
import { getStationById, getBikesByStationId } from "../api/stationApi";
import CommandMenu from "../components/commandMenu/CommandMenu";

const stations = [
  { id: "ST001", position: "45.5017,-73.5673", stationName: "Downtown Central", streetAddress: "123 Main St, Montreal, QC" },
  { id: "ST002", position: "45.5088,-73.5616", stationName: "Old Port East", streetAddress: "456 Elm St, Montreal, QC" },
  { id: "ST003", position: "45.495,-73.578", stationName: "Atwater Market", streetAddress: "789 Pine St, Montreal, QC" },
];

const Dashboard = () => {
  const [selectedStation, setSelectedStation] = useState(null);
  const [selectedDock, setSelectedDock] = useState(null);
  const [loading, setLoading] = useState(false);
  const [notifications, setNotifications] = useState([]);

  // Fetch full station data + all bikes
  const handleMarkerClick = async (stationId) => {
  const clickedStation = stations.find((s) => s.id === stationId);
  setSelectedStation({ stationName: clickedStation.stationName }); // open panel instantly
  setLoading(true);

  try {
    const [stationData, bikesData] = await Promise.all([
      getStationById(stationId),
      getBikesByStationId(stationId),
    ]);

    // Attach bikes to station
    stationData.bikes = bikesData;

    setSelectedStation(stationData);
  } catch (err) {
    console.error("Failed to fetch station or bike details:", err);
  } finally {
    setLoading(false);
  }
};


  return (
    <div className="dashboard-container">
      <div className="dashboard-legend">
        <div className="legend-item">
          <span className="legend-color red"></span>
          <span>Empty / Full (0% or 100%)</span>
        </div>
        <div className="legend-item">
          <span className="legend-color yellow"></span>
          <span>Almost Full (&lt;25% or &gt;85%)</span>
        </div>
        <div className="legend-item">
          <span className="legend-color green"></span>
          <span>Balanced</span>
        </div>
        <div className="legend-item">
          <span className="bike-type e-bike">E:</span>
          <span>E-Bike</span>
        </div>
        <div className="legend-item">
          <span className="bike-type standard-bike">S:</span>
          <span>Standard Bike</span>
        </div>
      </div>
      <div className="map-container">
        <MapView
          stations={stations}
          onStationClick={handleMarkerClick}
        />
        <SidePanel
          station={selectedStation}
          onClose={() => {
            setSelectedStation(null);
            setSelectedDock(null);
          }}
          loading={loading}
          onDockSelect={setSelectedDock}
        />
      </div>
      {selectedDock && (
        <CommandMenu
          station={selectedStation}
          dock={selectedDock}
          onClose={() => setSelectedDock(null)}
        />
      )}
    </div>
  );
};

export default Dashboard;
