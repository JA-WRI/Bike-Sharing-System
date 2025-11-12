import React, { useState, useEffect, useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import MapView from "../components/MapView";
import SidePanel from "../components/SidePanel";
import '../styles/Dashboard.css';
import '../styles/SidePanel.css';
import { getBikeById } from "../api/bikeApi";
import { getStationById, getBikesByStationId } from "../api/stationApi";
import CommandMenu from "../components/commandMenu/CommandMenu";
import { checkPaymentMethod } from "../api/paymentMethodApi";
import { getCurrentPlan } from "../api/planApi";

const stations = [
  { id: "ST001", position: "45.5017,-73.5673", stationName: "Downtown Central", streetAddress: "123 Main St, Montreal, QC", capacity: 5, occupancy: 3 },
  { id: "ST002", position: "45.5088,-73.5616", stationName: "Old Port East", streetAddress: "456 Elm St, Montreal, QC", capacity: 5, occupancy: 3 },
  { id: "ST003", position: "45.495,-73.578", stationName: "Atwater Market", streetAddress: "789 Pine St, Montreal, QC", capacity: 5, occupancy: 1 },
];

const Dashboard = () => {
  const { user } = useContext(AuthContext);
  const navigate = useNavigate();
  const [selectedStation, setSelectedStation] = useState(null);
  const [selectedDock, setSelectedDock] = useState(null);
  const [loading, setLoading] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const [errorMessage, setErrorMessage] = useState(null);

  // Fetch full station data + all bikes
  const fetchStationData = async (stationId) => {
    if (!stationId) return;
    
    setLoading(true);
    try {
      const [stationData, bikesData] = await Promise.all([
        getStationById(stationId),
        getBikesByStationId(stationId),
      ]);

      // Attach bikes to station
      stationData.bikes = bikesData;

      // Match bikes to docks
      for (const dockDTO of stationData.docks) {
        let bike = null;
        
        // First try to match by bikeId (dock has a bikeId field)
        if (dockDTO.bikeId) {
          bike = bikesData.find((b) => b.bikId === dockDTO.bikeId);
        }
        
        // If not found, try to match by dockId (bike has a dockId field)
        if (!bike) {
          bike = bikesData.find((b) => b.dockId === dockDTO.dockId);
        }
        
        dockDTO.bike = bike;
      }

      setSelectedStation(stationData);
    } catch (err) {
      console.error("Failed to fetch station or bike details:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleMarkerClick = async (stationId) => {
    const clickedStation = stations.find((s) => s.id === stationId);
    setSelectedStation({ stationName: clickedStation.stationName }); // open panel instantly
    await fetchStationData(stationId);
  };

  // Refresh station data (used after commands)
  const refreshStation = async () => {
    if (selectedStation?.id) {
      await fetchStationData(selectedStation.id);
    }
  };

  // Check if rider has payment method and plan
  const checkRiderPaymentSetup = async () => {
    if (!user || user.role !== "RIDER") {
      return { hasPaymentMethod: true, hasPaymentPlan: true }; // Not a rider, no check needed
    }

    try {
      const [hasPaymentMethod, paymentPlan] = await Promise.all([
        checkPaymentMethod(user.email),
        getCurrentPlan(user.email)
      ]);

      return {
        hasPaymentMethod,
        hasPaymentPlan: !!paymentPlan
      };
    } catch (error) {
      console.error("Error checking payment setup:", error);
      return { hasPaymentMethod: false, hasPaymentPlan: false };
    }
  };

  // Handle dock selection with authentication and payment check
  const handleDockSelect = async (dock) => {
    if (!user) {
      setErrorMessage("Please login to interact with stations");
      setTimeout(() => setErrorMessage(null), 4000);
      return;
    }

    // For riders, check payment method and plan
    if (user.role === "RIDER") {
      const { hasPaymentMethod, hasPaymentPlan } = await checkRiderPaymentSetup();
      
      if (!hasPaymentMethod) {
        setErrorMessage("Please add a payment method before using bike services");
        setTimeout(() => {
          setErrorMessage(null);
          navigate("/add-payment");
        }, 3000);
        return;
      }

      if (!hasPaymentPlan) {
        setErrorMessage("Please select a payment plan before using bike services");
        setTimeout(() => {
          setErrorMessage(null);
          navigate("/payment-plans");
        }, 3000);
        return;
      }
    }

    setSelectedDock(dock);
  };


  return (
    <div className="dashboard-container">
      {errorMessage && (
        <div className="dashboard-error-message">
          {errorMessage}
        </div>
      )}
      <div className="dashboard-header">
        <h1 className="dashboard-title">VeloMTL</h1>
        <p className="dashboard-slogan">Your Ride, Your City, Your Way</p>
      </div>
      <div className="map-container">
        <div className="map-wrapper-container">
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
          <div className="map-wrapper">
            <MapView
              stations={stations}
              onStationClick={handleMarkerClick}
            />
          </div>
        </div>
        <SidePanel
          station={selectedStation}
          onClose={() => {
            setSelectedStation(null);
            setSelectedDock(null);
          }}
          loading={loading}
          onDockSelect={handleDockSelect}
        />
      </div>
      {selectedDock && (
        <CommandMenu
          station={selectedStation}
          dock={selectedDock}
          onClose={() => setSelectedDock(null)}
          onCommandSuccess={refreshStation}
        />
      )}
    </div>
  );
};

export default Dashboard;
