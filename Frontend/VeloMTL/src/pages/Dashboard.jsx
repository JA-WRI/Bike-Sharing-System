import React, { useState } from "react";
import MapView from "../components/MapView";
import SidePanel from "../components/SidePanel";
import '../styles/map.css';
import '../styles/SidePanel.css';

const stations = [
  { id: "ST001", position: "45.5017,-73.5673", stationName: "Downtown Central", streetAddress: "123 Main St, Montreal, QC" },
  { id: "ST002", position: "45.5088,-73.5616", stationName: "Old Port East", streetAddress: "456 Elm St, Montreal, QC" },
  { id: "ST003", position: "45.495,-73.578", stationName: "Atwater Market", streetAddress: "789 Pine St, Montreal, QC" },
];

const Dashboard = () => {
  const [selectedStation, setSelectedStation] = useState(null);
  const [loading, setLoading] = useState(false);

  // Fetch full station info
  const fetchStationDetails = async (stationId) => {
    try {
      setLoading(true);
      const response = await fetch(`http://localhost:8080/stations/${stationId}`);
      if (!response.ok) throw new Error("Failed to fetch station data");
      const data = await response.json();
      setSelectedStation(data); // Update side panel with fetched info
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  // Called when marker is clicked
  const handleMarkerClick = (stationId) => {
    // Show loading state immediately
    setSelectedStation({ id: stationId });
    fetchStationDetails(stationId);
  };

  return (
    <div className="dashboard-container">
      <h1 className="dashboard-title">Welcome to the Dashboard</h1>
      <div className="map-container">
        <MapView
          stations={stations}
          onStationClick={handleMarkerClick}
        />
        <SidePanel
          station={selectedStation}
          onClose={() => setSelectedStation(null)}
          loading={loading}
        />
      </div>
    </div>
  );
};

export default Dashboard;
