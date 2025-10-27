import React from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import L from 'leaflet';
import "leaflet-providers";
import 'leaflet/dist/leaflet.css';
import iconUrl from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

const JAWG_TOKEN = import.meta.env.VITE_JAWG_TOKEN;

// Fix for default marker icons
const DefaultIcon = L.icon({ iconUrl, shadowUrl: iconShadow });
L.Marker.prototype.options.icon = DefaultIcon;

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
