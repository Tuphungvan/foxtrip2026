import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import './Header.css';

const Header = () => {
  const { user, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  return (
    <header className="header">
      <div className="header-content">
        <Link to="/" className="logo">
          Fox<span>Trip</span>
        </Link>

        <nav className="navbar">
          <Link to="/">Trang chủ</Link>
          <Link to="/tours">Tours</Link>
          <Link to="/hotels">Khách sạn</Link>
          <Link to="/restaurants">Nhà hàng</Link>
          <Link to="/transportation">Phương tiện</Link>
        </nav>

        <div className="header-actions">
          {isAuthenticated ? (
            <>
              <Link to="/profile" className="user-link">
                <i className="fas fa-user"></i>
                <span>{user?.fullName || user?.username}</span>
              </Link>
              <button onClick={handleLogout} className="btn-logout">
                Đăng xuất
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="btn btn-outline">
                Đăng nhập
              </Link>
              <Link to="/register" className="btn btn-primary">
                Đăng ký
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header;
