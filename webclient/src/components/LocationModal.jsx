import { useEffect, useRef } from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import './LocationModal.css';

// Fix default marker icon issue with Leaflet + Webpack
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
  iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
});

// Custom orange marker icon
const orangeIcon = new L.Icon({
  iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-orange.png',
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41]
});

const LocationModal = ({ isOpen, onClose, location }) => {
  if (!isOpen || !location) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Chi nhánh {location.name}</h2>
          <button className="modal-close" onClick={onClose}>
            <i className="fas fa-times"></i>
          </button>
        </div>
        <div className="modal-body">
          <div className="map-container">
            <MapContainer
              center={[location.lat, location.lng]}
              zoom={15}
              style={{ height: '100%', width: '100%' }}
              scrollWheelZoom={true}
            >
              <TileLayer
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
              />
              <Marker position={[location.lat, location.lng]} icon={orangeIcon}>
                <Popup>
                  <div style={{ padding: '5px' }}>
                    <h3 style={{ margin: '0 0 8px 0', color: '#333', fontSize: '16px' }}>
                      FoxTrip {location.name}
                    </h3>
                    <p style={{ margin: '0', color: '#666', fontSize: '14px' }}>
                      {location.address}
                    </p>
                    <p style={{ margin: '8px 0 0 0', color: '#ffa500', fontSize: '13px' }}>
                      📞 {location.phone}
                    </p>
                  </div>
                </Popup>
              </Marker>
            </MapContainer>
          </div>
          <div className="location-info">
            <div className="info-item">
              <i className="fas fa-map-marker-alt"></i>
              <span>{location.address}</span>
            </div>
            <div className="info-item">
              <i className="fas fa-phone"></i>
              <span>{location.phone}</span>
            </div>
            <div className="info-item">
              <i className="fas fa-envelope"></i>
              <span>{location.email}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LocationModal;
