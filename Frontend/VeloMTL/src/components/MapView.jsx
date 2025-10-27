import React from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import L from 'leaflet';
import "leaflet-providers";
const JAWG_TOKEN = import.meta.env.VITE_JAWG_TOKEN;

import 'leaflet/dist/leaflet.css';
import iconUrl from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

let DefaultIcon = L.icon({
  iconUrl,
  shadowUrl: iconShadow,
});
L.Marker.prototype.options.icon = DefaultIcon;

const MapView = ({ stations, onStationClick }) => {
  return (
    <MapContainer
      center={[45.5017, -73.5660]} // initial map center
      zoom={13.5}
      style={{ height: "100vh", width: "100%" }}
    >
      <TileLayer
        url={`https://tile.jawg.io/jawg-streets/{z}/{x}/{y}{r}.png?access-token=${JAWG_TOKEN}`}
        attribution='&copy; <a href="https://www.jawg.io" target="_blank" rel="noopener noreferrer">Jawg</a> contributors'
      />
      {stations.map(station => (
        <Marker 
        key={station.id} 
        position={station.position}
        eventHandlers={{
          click: () => onStationClick(station)
        }}>
          <Popup>{station.name}</Popup>
        </Marker>
      ))}
    </MapContainer>
  );
};

export default MapView;
