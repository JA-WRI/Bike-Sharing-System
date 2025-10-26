import React from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import L from 'leaflet';

import 'leaflet/dist/leaflet.css';
import iconUrl from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

let DefaultIcon = L.icon({
  iconUrl,
  shadowUrl: iconShadow,
});
L.Marker.prototype.options.icon = DefaultIcon;

const stations = [
  { id: 1, name: "Station A", position: [45.5017, -73.5673] }, // Montreal downtown
  { id: 2, name: "Station B", position: [45.5088, -73.5616] },
  { id: 3, name: "Station C", position: [45.4950, -73.5780] },
];

const MapView = () => {
  return (
    <MapContainer
      center={[45.5017, -73.5673]} // initial map center
      zoom={13}
      style={{ height: "100vh", width: "100%" }}
    >
      <TileLayer
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        attribution="&copy; OpenStreetMap contributors"
      />
      {stations.map(station => (
        <Marker key={station.id} position={station.position}>
          <Popup>{station.name}</Popup>
        </Marker>
      ))}
    </MapContainer>
  );
};

export default MapView;
