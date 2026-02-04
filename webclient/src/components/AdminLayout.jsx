import { Link, Outlet, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import './AdminLayout.css';

const AdminLayout = () => {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  const toggleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };

  return (
    <div className="admin-layout">
      <button className="hamburger" onClick={toggleSidebar}>
        ☰
      </button>

      <aside className={`sidebar ${sidebarOpen ? 'open' : ''}`}>
        <div className="sidebar-header">
          <h2>FoxTrip Admin</h2>
        </div>

        <div className="profile-section">
          <i className="fas fa-user-circle"></i>
          <span>{user?.username || 'Admin'}</span>
        </div>

        <nav className="admin-menu">
          <ul>
            <li>
              <Link to="/admin/dashboard" onClick={() => setSidebarOpen(false)}>
                <i className="fas fa-tachometer-alt"></i> Dashboard
              </Link>
            </li>
            <li>
              <Link to="/admin/tours" onClick={() => setSidebarOpen(false)}>
                <i className="fas fa-map-marker-alt"></i> Quản lý Tour
              </Link>
            </li>
            <li>
              <Link to="/admin/users" onClick={() => setSidebarOpen(false)}>
                <i className="fas fa-users"></i> Quản lý người dùng
              </Link>
            </li>
            <li>
              <Link to="/admin/bookings" onClick={() => setSidebarOpen(false)}>
                <i className="fas fa-bookmark"></i> Quản lý đơn hàng
              </Link>
            </li>
            <li>
              <Link to="/admin/reports" onClick={() => setSidebarOpen(false)}>
                <i className="fas fa-chart-line"></i> Báo cáo doanh thu
              </Link>
            </li>
          </ul>
        </nav>

        <div className="sidebar-footer">
          <button onClick={handleLogout} className="logout-btn">
            <i className="fas fa-sign-out-alt"></i> Đăng xuất
          </button>
        </div>
      </aside>

      <div className="admin-content">
        <Outlet />
      </div>
    </div>
  );
};

export default AdminLayout;
