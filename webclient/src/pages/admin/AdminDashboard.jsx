import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { tourService } from '../../services/tourService';
import './AdminDashboard.css';

const AdminDashboard = () => {
  const [stats, setStats] = useState({
    totalTours: 0,
    activeTours: 0,
    totalBookings: 0,
    totalRevenue: 0,
  });
  const [recentTours, setRecentTours] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);
      
      // Fetch tours
      const toursResponse = await tourService.getAllTours(0, 5);
      const toursData = toursResponse.data;
      
      setStats({
        totalTours: toursData.totalElements || 0,
        activeTours: toursData.content?.filter(t => t.isBookable).length || 0,
        totalBookings: 0, // TODO: Implement bookings API
        totalRevenue: 0, // TODO: Implement revenue API
      });
      
      setRecentTours(toursData.content || []);
    } catch (error) {
      console.error('Failed to fetch dashboard data:', error);
    } finally {
      setLoading(false);
    }
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
    }).format(amount);
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString('vi-VN');
  };

  if (loading) {
    return (
      <div className="admin-dashboard">
        <div className="loading-container">
          <div className="spinner"></div>
          <p>Đang tải dữ liệu...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="admin-dashboard">
      <div className="dashboard-header">
        <div>
          <h1 className="dashboard-title">Dashboard</h1>
          <p className="dashboard-subtitle">Tổng quan hệ thống quản trị FoxTrip</p>
        </div>
        <Link to="/admin/tours/new" className="btn btn-primary">
          <i className="fas fa-plus"></i> Tạo Tour Mới
        </Link>
      </div>

      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-icon tours">
            <i className="fas fa-map-marked-alt"></i>
          </div>
          <div className="stat-info">
            <h3>{stats.totalTours}</h3>
            <p>Tổng số Tour</p>
            <span className="stat-detail">{stats.activeTours} đang hoạt động</span>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon users">
            <i className="fas fa-users"></i>
          </div>
          <div className="stat-info">
            <h3>Keycloak</h3>
            <p>Quản lý User</p>
            <a 
              href="http://localhost:8080/admin/master/console/#/foxtrip" 
              target="_blank" 
              rel="noopener noreferrer"
              className="stat-link"
            >
              Mở Keycloak Admin →
            </a>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon bookings">
            <i className="fas fa-bookmark"></i>
          </div>
          <div className="stat-info">
            <h3>{stats.totalBookings}</h3>
            <p>Đơn đặt tour</p>
            <span className="stat-detail">Đang phát triển</span>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon revenue">
            <i className="fas fa-dollar-sign"></i>
          </div>
          <div className="stat-info">
            <h3>{formatCurrency(stats.totalRevenue)}</h3>
            <p>Doanh thu</p>
            <span className="stat-detail">Đang phát triển</span>
          </div>
        </div>
      </div>

      <div className="dashboard-sections">
        <div className="section-card full-width">
          <div className="section-header">
            <h2>
              <i className="fas fa-clock"></i> Tour gần đây
            </h2>
            <Link to="/admin/tours" className="btn btn-secondary">
              Xem tất cả
            </Link>
          </div>
          
          {recentTours.length === 0 ? (
            <div className="empty-state">
              <i className="fas fa-map-marked-alt"></i>
              <p>Chưa có tour nào</p>
              <Link to="/admin/tours/new" className="btn btn-primary">
                Tạo tour đầu tiên
              </Link>
            </div>
          ) : (
            <div className="tours-table">
              <table>
                <thead>
                  <tr>
                    <th>Tên Tour</th>
                    <th>Địa điểm</th>
                    <th>Ngày khởi hành</th>
                    <th>Giá</th>
                    <th>Chỗ còn lại</th>
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                  </tr>
                </thead>
                <tbody>
                  {recentTours.map((tour) => (
                    <tr key={tour.id}>
                      <td>
                        <div className="tour-name-cell">
                          {tour.images && tour.images.length > 0 && (
                            <img src={tour.images[0]} alt={tour.name} className="tour-thumb" />
                          )}
                          <span>{tour.name}</span>
                        </div>
                      </td>
                      <td>{tour.province}</td>
                      <td>{formatDate(tour.startDate)}</td>
                      <td>{formatCurrency(tour.finalPrice || tour.price)}</td>
                      <td>
                        <span className={`slots ${tour.availableSlots === 0 ? 'full' : ''}`}>
                          {tour.availableSlots}/{tour.slots}
                        </span>
                      </td>
                      <td>
                        <span className={`status-badge ${tour.isBookable ? 'active' : 'inactive'}`}>
                          {tour.isBookable ? 'Hoạt động' : 'Tạm dừng'}
                        </span>
                      </td>
                      <td>
                        <div className="action-buttons">
                          <Link to={`/admin/tours/edit/${tour.id}`} className="btn-icon" title="Sửa">
                            <i className="fas fa-edit"></i>
                          </Link>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>

        <div className="section-card">
          <h2>
            <i className="fas fa-chart-line"></i> Thống kê nhanh
          </h2>
          <div className="quick-stats">
            <div className="quick-stat-item">
              <span className="label">Tour phổ biến nhất:</span>
              <span className="value">Đang phát triển</span>
            </div>
            <div className="quick-stat-item">
              <span className="label">Tỷ lệ đặt tour:</span>
              <span className="value">Đang phát triển</span>
            </div>
            <div className="quick-stat-item">
              <span className="label">Đánh giá trung bình:</span>
              <span className="value">Đang phát triển</span>
            </div>
          </div>
        </div>

        <div className="section-card">
          <h2>
            <i className="fas fa-tools"></i> Công cụ quản trị
          </h2>
          <div className="admin-tools">
            <a 
              href="http://localhost:8080/admin/master/console/#/foxtrip" 
              target="_blank" 
              rel="noopener noreferrer"
              className="tool-link"
            >
              <i className="fas fa-users-cog"></i>
              <span>Keycloak Admin Console</span>
              <small>Quản lý users, roles, authentication</small>
            </a>
            <a 
              href="http://localhost:8081/swagger-ui/index.html" 
              target="_blank" 
              rel="noopener noreferrer"
              className="tool-link"
            >
              <i className="fas fa-code"></i>
              <span>API Documentation</span>
              <small>Swagger UI - Test APIs</small>
            </a>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
