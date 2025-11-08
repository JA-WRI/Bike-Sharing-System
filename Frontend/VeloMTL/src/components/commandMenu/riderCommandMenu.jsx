// src/components/CommandMenu/RiderCommandMenu.jsx
import React, { useContext, useState, useEffect } from "react";
import { AuthContext } from "../../context/AuthContext";
import { reserveDock, reserveBike, unlockBike, lockBike } from "../../api/riderApi";

const getLocalISODateTime = (date) => {
    const pad = (num) => num.toString().padStart(2, '0');

    const year = date.getFullYear();
    const month = pad(date.getMonth() + 1); // Month is 0-indexed
    const day = pad(date.getDate());
    const hours = pad(date.getHours());
    const minutes = pad(date.getMinutes());
    const seconds = pad(date.getSeconds());

    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
};

const RESERVE_TIME = 15*60*1000; // 15 mins as MS

const RiderCommandMenu = ({ station, dock, setResponseMessage, setResponseStatus, onCommandSuccess }) => {
  const { user } = useContext(AuthContext);
  const [timerStarted, setTimerStarted] = useState(false);

  
  const startTimer = () => {
    clearTimeout();
    setTimeout(() => {
      alert("Reservation time has expired.");
      setTimerStarted(false);
    }, RESERVE_TIME);
  };

  useEffect(() => {
    if (timerStarted) {
      startTimer();
    }
    return () => clearTimeout();
  }, [timerStarted]);

  const handleCommand = async (action, ...args) => {
    let extraParams = {};

    if (action === "LB") {
      const bikeId = prompt("Enter the Bike ID to lock:");
      if (!bikeId) {
        alert("Bike ID is required to lock the bike.");
        return;
      }
      extraParams.bikeId = bikeId;
    }

    console.log(`Command "${action}" sent for dock ${dock.dockId}`);

    try {
      let response;

      switch (action) {
        case "RB":
          response = await reserveBike(user.id, dock.bikeId, dock.dockId, getLocalISODateTime(new Date()));
          if (response.status === "SUCCESS") setTimerStarted(true);
          break;
        case "RD":
          response = await reserveDock(user.id, dock.bikeId, dock.dockId, getLocalISODateTime(new Date()));
          if (response.status === "SUCCESS") setTimerStarted(true);
          break;
        case "UB":
          response = await unlockBike(user.id, dock.bikeId, dock.dockId);
          break;
        case "LB":
          response = await lockBike(user.id, extraParams.bikeId, dock.dockId);
          break;
        default:
          console.warn("Unknown operator command:", action);
      }

      console.log("Command response:", response);
      setResponseMessage(response.message);
      setResponseStatus(response.status);
      
      // Refresh station data if command was successful
      if (response.status === "SUCCESS" && onCommandSuccess) {
        // Small delay to ensure backend has processed the change
        setTimeout(() => {
          onCommandSuccess();
        }, 500);
      }
    } catch (err) {
      console.error("Command failed:", err);
      setResponseMessage(err?.response?.data?.error || "An error occurred while performing the command.");
      setResponseStatus("ERROR");
    }
  };

  return (
    <>
      <div className="command-section">
        <h3>Dock: {dock.dockId}</h3>
        <div className="command-buttons">
          <button onClick={() => handleCommand("RD")} className="command-btn primary">
            Reserve Dock
          </button>
        </div>
      </div>

      <div className="command-section">
        <h3>Bike: {dock.bikeId}</h3>
        <div className="command-buttons">
          <button onClick={() => handleCommand("RB")} className="command-btn primary">
            Reserve Bike
          </button>
          <button onClick={() => handleCommand("UB")} className="command-btn success">
            Unlock Bike
          </button>
          <button onClick={() => handleCommand("LB")} className="command-btn secondary">
            Lock Bike
          </button>
        </div>
      </div>
    </>
  );
};

export default RiderCommandMenu;
