import { Link } from 'react-router-dom';
import './Home.css';

const Home = () => {
  return (
    <div className="home-page">
      <section className="hero-section">
        <div className="hero-content">
          <h1 className="hero-title">
            Khám phá <span>Việt Nam</span> cùng FoxTrip
          </h1>
          <p className="hero-subtitle">
            Đặt tour, khách sạn, nhà hàng và phương tiện di chuyển dễ dàng
          </p>
          <div className="hero-actions">
            <Link to="/tours" className="btn btn-primary btn-lg">
              Khám phá Tours
            </Link>
            <Link to="/hotels" className="btn btn-outline btn-lg">
              Tìm khách sạn
            </Link>
          </div>
        </div>
      </section>

      <section className="features-section">
        <div className="container">
          <h2 className="section-title">Dịch vụ của chúng tôi</h2>
          
          <div className="features-grid">
            <div className="feature-card">
              <div className="feature-icon">
                <i className="fas fa-map-marked-alt"></i>
              </div>
              <h3>Tours du lịch</h3>
              <p>Khám phá các tour du lịch hấp dẫn khắp Việt Nam</p>
              <Link to="/tours" className="feature-link">
                Xem thêm <i className="fas fa-arrow-right"></i>
              </Link>
            </div>

            <div className="feature-card">
              <div className="feature-icon">
                <i className="fas fa-hotel"></i>
              </div>
              <h3>Khách sạn</h3>
              <p>Đặt phòng khách sạn với giá tốt nhất</p>
              <Link to="/hotels" className="feature-link">
                Xem thêm <i className="fas fa-arrow-right"></i>
              </Link>
            </div>

            <div className="feature-card">
              <div className="feature-icon">
                <i className="fas fa-utensils"></i>
              </div>
              <h3>Nhà hàng</h3>
              <p>Đặt bàn tại các nhà hàng nổi tiếng</p>
              <Link to="/restaurants" className="feature-link">
                Xem thêm <i className="fas fa-arrow-right"></i>
              </Link>
            </div>

            <div className="feature-card">
              <div className="feature-icon">
                <i className="fas fa-bus"></i>
              </div>
              <h3>Phương tiện</h3>
              <p>Đặt vé xe, tàu đi khắp mọi nơi</p>
              <Link to="/transportation" className="feature-link">
                Xem thêm <i className="fas fa-arrow-right"></i>
              </Link>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;
