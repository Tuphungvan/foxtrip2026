import { useState } from 'react';
import LocationModal from './LocationModal';
import { OFFICE_LOCATIONS } from '../data/locations';
import './Footer.css';

const Footer = () => {
  const [selectedLocation, setSelectedLocation] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleLocationClick = (locationKey) => {
    setSelectedLocation(OFFICE_LOCATIONS[locationKey]);
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setTimeout(() => setSelectedLocation(null), 300);
  };

  return (
    <>
      <footer className="footer">
        <div className="footer-container">
          <div className="footer-box">
            <h3>Về chúng tôi</h3>
            <p>
              FoxTrip là nền tảng đặt tour trực tuyến mang đến trải nghiệm du lịch 
              thông minh, tiện lợi với hàng ngàn tour chất lượng trong nước và quốc tế.
            </p>
            <p>Liên hệ: 0859 605 024</p>
          </div>

          <div className="footer-box">
            <h3>Chi nhánh trong nước</h3>
            <a onClick={() => handleLocationClick('hanoi')}>
              <i className="fas fa-map-marker-alt"></i> Hà Nội
            </a>
            <a onClick={() => handleLocationClick('hcm')}>
              <i className="fas fa-map-marker-alt"></i> Hồ Chí Minh
            </a>
            <a onClick={() => handleLocationClick('danang')}>
              <i className="fas fa-map-marker-alt"></i> Đà Nẵng
            </a>
            <a onClick={() => handleLocationClick('nhatrang')}>
              <i className="fas fa-map-marker-alt"></i> Nha Trang
            </a>
            <a onClick={() => handleLocationClick('phuquoc')}>
              <i className="fas fa-map-marker-alt"></i> Phú Quốc
            </a>
          </div>

          <div className="footer-box">
            <h3>Đường dẫn nhanh</h3>
            <a href="/">Trang chủ</a>
            <a href="/tours">Tour</a>
            <a href="/profile">Hồ sơ</a>
            <a href="/about">Về chúng tôi</a>
          </div>

          <div className="footer-box">
            <h3>Theo dõi chúng tôi</h3>
            <a href="https://www.facebook.com" target="_blank" rel="noopener noreferrer">
              <i className="fab fa-facebook"></i> Facebook
            </a>
            <a href="https://www.instagram.com" target="_blank" rel="noopener noreferrer">
              <i className="fab fa-instagram"></i> Instagram
            </a>
            <a href="https://www.twitter.com" target="_blank" rel="noopener noreferrer">
              <i className="fab fa-twitter"></i> Twitter
            </a>
          </div>
        </div>

        <div className="footer-bottom">
          <p>&copy; {new Date().getFullYear()} FoxTrip. All rights reserved.</p>
        </div>
      </footer>

      <LocationModal 
        isOpen={isModalOpen}
        onClose={closeModal}
        location={selectedLocation}
      />
    </>
  );
};

export default Footer;
