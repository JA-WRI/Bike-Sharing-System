// src/components/CommandMenu/RiderCommandMenu.jsx
import React, { useContext, useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../../context/AuthContext";
import { reserveDock, reserveBike, unlockBike, lockBike, getTierByEmail } from "../../api/riderApi";
import { checkPaymentMethod } from "../../api/paymentMethodApi";
import { getCurrentPlan } from "../../api/planApi";


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
  const { user, activeRole, update } = useContext(AuthContext);
  const navigate = useNavigate();
  const [timerStarted, setTimerStarted] = useState(false);
  const [timeoutId, setTimeoutId] = useState(null);

  // Check if rider has payment method and plan
  const checkRiderPaymentSetup = async () => {
    if (!user || activeRole !== "RIDER") {
      return { hasPaymentMethod: true, hasPaymentPlan: true };
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

  
  const startTimer = (extraHoldMins) => {
    const id = setTimeout(() => {
      getTierByEmail(user.email).then(tierData => {
        update({ tier: tierData.newTier });
        // if (tierData.tierChanged) alert(`Your Tier was changed from ${tierData.oldTier} to ${tierData.newTier}.`);
      });
      alert("Reservation time has expired.");
      setTimerStarted(false);
    }, RESERVE_TIME + (extraHoldMins*60*1000 || 0));
    setTimeoutId(id);
  };

  useEffect(() => {
    if (timerStarted) {
      userTier = user?.tier?.toLowerCase();
      extraHoldMins = userTier === "gold" ? 5 : userTier === "silver" ? 2 : 0;
      startTimer(extraHoldMins);
    }
    return () => {
      if (timeoutId) {
        clearTimeout(timeoutId);
      }
    };
  }, [timerStarted]);

  const handleCommand = async (action, ...args) => {
    // Validate payment setup before executing any command
    if (activeRole === "RIDER") {
      const { hasPaymentMethod, hasPaymentPlan } = await checkRiderPaymentSetup();
      
      if (!hasPaymentMethod) {
        setResponseMessage("Please add a payment method before using bike services");
        setResponseStatus("ERROR");
        setTimeout(() => {
          navigate("/add-payment");
        }, 2000);
        return;
      }

      if (!hasPaymentPlan) {
        setResponseMessage("Please select a payment plan before using bike services");
        setResponseStatus("ERROR");
        setTimeout(() => {
          navigate("/payment-plans");
        }, 2000);
        return;
      }
    }

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
          const tierData = await getTierByEmail(user.email);
          if (tierData.tierChanged) {
            update({ tier: tierData.newTier });
            // alert(`Your Tier was changed from ${tierData.oldTier} to ${tierData.newTier}.`);
          }
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
