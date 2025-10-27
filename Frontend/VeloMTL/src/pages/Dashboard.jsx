import React, { useState } from "react";
import MapView from "../components/MapView"
import SidePanel from "../components/SidePanel";
import '../styles/map.css'
import '../styles/SidePanel.css'

const stations = [
  { id: 1, name: "Station A", position: [45.5017, -73.5673] },
  { id: 2, name: "Station B", position: [45.5088, -73.5616] },
  { id: 3, name: "Station C", position: [45.495, -73.578] },
];

const Dashboard = () => {
  const [selectedStation, setSelectedStation] = useState(null);

  return (
    <div className="dashboard-container">
      <h1 className="dashboard-title">Welcome to the Dashboard</h1>
      <div className="map-container ">
        <div className="map-wrapper">
        <MapView
          stations={stations}
          onStationClick={(station) => setSelectedStation(station)}
        />
        </div>
        <SidePanel
          station={selectedStation}
          onClose={() => setSelectedStation(null)}
        />
      </div>
    </div>
  );
};

export default Dashboard;