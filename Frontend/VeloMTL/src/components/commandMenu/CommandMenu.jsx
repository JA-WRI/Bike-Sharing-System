import React, { useContext, useState, useEffect } from "react";
import { AuthContext } from "../../context/AuthContext";
import OperatorCommandMenu from "./operatorCommandMenu";
import RiderCommandMenu from "./riderCommandMenu";
import "../../styles/CommandMenu.css";

const CommandMenu = ({ station, dock, onClose, onCommandSuccess }) => {
  const { user } = useContext(AuthContext);
  const [responseMessage, setResponseMessage] = useState(null);
  const [responseStatus, setResponseStatus] = useState(null);
  if (!dock || !user) return null;

  useEffect(() => {
    if (responseMessage) {
      const timer = setTimeout(() => setResponseMessage(null), 4000);
      return () => clearTimeout(timer);
    }
  }, [responseMessage]);

  return (
    <div className="command-menu-overlay">
      <div className="command-menu-modal">
        <div className="command-menu-header">
          <h2 className="command-title">{station.stationName || "Station"}</h2>
          <button className="command-close-btn" onClick={onClose}>
            ✕
          </button>
        </div>

        {responseMessage && (
          <div
            className={`command-response ${
              responseStatus === "SUCCESS" ? "success" : "error"
            }`}
          >
            {responseMessage}
          </div>
        )}

        {user.role === "OPERATOR" ? (
          <OperatorCommandMenu
            station={station} 
            dock={dock} 
            setResponseMessage={setResponseMessage}
            setResponseStatus={setResponseStatus}
            onCommandSuccess={onCommandSuccess}
            />
        ) : (
          <RiderCommandMenu
             station={station} 
             dock={dock} 
             setResponseMessage={setResponseMessage}
             setResponseStatus={setResponseStatus}
             onCommandSuccess={onCommandSuccess}
             />
        )}
      </div>
    </div>
  );
};

export default CommandMenu;
