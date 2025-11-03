import React, { useState, useEffect } from "react";
import MapView from "../components/MapView";
import SidePanel from "../components/SidePanel";
import '../styles/map.css';
import '../styles/SidePanel.css';
import { getStationById } from "../api/stationApi";

const stations = [
  { id: "ST001", position: "45.5017,-73.5673", stationName: "Downtown Central", streetAddress: "123 Main St, Montreal, QC" },
  { id: "ST002", position: "45.5088,-73.5616", stationName: "Old Port East", streetAddress: "456 Elm St, Montreal, QC" },
  { id: "ST003", position: "45.495,-73.578", stationName: "Atwater Market", streetAddress: "789 Pine St, Montreal, QC" },
];

const Dashboard = () => {
  const [selectedStation, setSelectedStation] = useState(null);
  const [loading, setLoading] = useState(false);

  // Fetch all stations 
  const handleMarkerClick = async (stationId) => {
    setLoading(true);
    try {
      const data = await getStationById(stationId);
      setSelectedStation(data);
    } catch (err) {
      console.error("Failed to fetch station details:", err);
    } finally {
      setLoading(false);
    }
  };


  return (
    <div className="dashboard-container">
      <h1 className="dashboard-title"></h1>
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
