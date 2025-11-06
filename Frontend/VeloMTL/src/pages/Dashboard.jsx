import React, { useState, useEffect, useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import MapView from "../components/MapView";
import SidePanel from "../components/SidePanel";
import '../styles/Dashboard.css';
import '../styles/SidePanel.css';
import { getStationById } from "../api/stationApi";
import { getBikeById } from "../api/bikeApi";
import CommandMenu from "../components/commandMenu/CommandMenu";

const stations = [
  { id: "ST001", position: "45.5017,-73.5673", stationName: "Downtown Central", streetAddress: "123 Main St, Montreal, QC", capacity: 5, occupancy: 3 },
  { id: "ST002", position: "45.5088,-73.5616", stationName: "Old Port East", streetAddress: "456 Elm St, Montreal, QC", capacity: 5, occupancy: 3 },
  { id: "ST003", position: "45.495,-73.578", stationName: "Atwater Market", streetAddress: "789 Pine St, Montreal, QC", capacity: 5, occupancy: 1 },
];

const Dashboard = () => {
  const [selectedStation, setSelectedStation] = useState(null);
  const [selectedDock, setSelectedDock] = useState(null);
  const [loading, setLoading] = useState(false);
  const [notifications, setNotifications] = useState([]);

  // Fetch all stations 
const handleMarkerClick = async (stationId) => {
  // Instantly open the panel with minimal info
  const clickedStation = stations.find((s) => s.id === stationId);
  setSelectedStation({ stationName: clickedStation.stationName }); // Open right away
  setLoading(true);

  try {
    const data = await getStationById(stationId);
    for (const dockDTO of data.docks) {
      if (dockDTO.bikeId) {
        dockDTO.bike = await getBikeById(dockDTO.bikeId);
      }
    }
    setSelectedStation(data); // Replace placeholder with full data
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
