import React from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import L from 'leaflet';
import "leaflet-providers";
import 'leaflet/dist/leaflet.css';
import iconUrl from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

const JAWG_TOKEN = import.meta.env.VITE_JAWG_TOKEN;

const DefaultIcon = L.icon({ iconUrl, shadowUrl: iconShadow });
L.Marker.prototype.options.icon = DefaultIcon;

// Create colored icons
const createColoredIcon = (color) =>
  L.divIcon({
    className: "custom-marker",
    html: `<div style="
      background-color:${color};
      width:18px;
      height:18px;
      border-radius:50%;
      border:2px solid white;
      box-shadow:0 0 5px rgba(0,0,0,0.3);
    "></div>`,
    iconSize: [18, 18],
    iconAnchor: [9, 9],
  });

  const getMarkerColor = (station) => {
  if (!station.capacity || station.capacity === 0) return "#7f8c8d"; // gray fallback

  const fullness = (station.occupancy / station.capacity) * 100;

  if (fullness === 0 || fullness === 100) return "#e74c3c"; // red
  if (fullness < 25 || fullness > 85) return "#f1c40f";     // yellow
  return "#2ecc71";                                          // green
};

const MapView = ({ stations = [], onStationClick }) => {
  return (
    <MapContainer
      center={[45.5017, -73.5660]}
      zoom={13.5}
      className="map-container"
    >
      <TileLayer
        url={`https://tile.jawg.io/jawg-streets/{z}/{x}/{y}{r}.png?access-token=${JAWG_TOKEN}`}
        attribution='&copy; <a href="https://www.jawg.io" target="_blank" rel="noopener noreferrer">Jawg</a> contributors'
      />
      {stations.map((station) => {
        if (!station?.position) return null;
        const [lat, lng] = station.position.split(',').map(Number);
        if (isNaN(lat) || isNaN(lng)) return null;

        return (
          <Marker
            key={station.id}
            position={[lat, lng]}
            eventHandlers={{ click: () => onStationClick(station.id) }}
          >
            <Popup>
              <strong>{station.stationName  || "Loading..."}</strong><br />
              {station.streetAddress || "Loading..."}
            </Popup>
          </Marker>
        );
      })}
    </MapContainer>
  );
};

export default MapView;
