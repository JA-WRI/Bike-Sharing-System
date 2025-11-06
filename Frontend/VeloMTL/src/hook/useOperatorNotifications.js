import { useEffect } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

const useOperatorNotifications = (onMessage) => {
  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) return;

    console.log("Opening WebSocket connection...");

    // Pass token in the query string for backend validation
    const socket = new SockJS(`http://localhost:8080/ws?token=${token}`);

    const stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      debug: (msg) => console.log(msg),

      onConnect: () => {
        console.log("Connected to WebSocket");
        stompClient.subscribe("/topic/operator", (message) => {
          console.log("New message:", message.body);
          
          onMessage(message.body);
        });
      },

      onStompError: (error) => {
        console.error("STOMP error:", error);
      },
    });

    stompClient.activate();

    return () => {
      stompClient.deactivate();
    };
  }, [onMessage]);
};

export default useOperatorNotifications;
