import { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { tourService } from '../../services/tourService';
import './TourList.css';

const TourList = () => {
  const [tours, setTours] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [searchKeyword, setSearchKeyword] = useState('');
  const navigate = useNavigate();

  const fetchTours = async (page = 0) => {
    try {
      setLoading(true);
      const response = await tourService.getAllTours(page, 10);
      
      if (response.success && response.data) {
        setTours(response.data.content || []);
        setTotalPages(response.data.totalPages || 0);
        setCurrentPage(page);
      }
    } catch (err) {
      setError('Không thể tải danh sách tour');
      console.error('Error fetching tours:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTours();
  }, []);

  const handleDelete = async (id) => {
    if (!window.confirm('Bạn có chắc chắn muốn xóa tour này?')) {
      return;
    }

    try {
      await tourService.deleteTour(id);
      alert('Xóa tour thành công!');
      fetchTours(currentPage);
    } catch (err) {
      alert('Không thể xóa tour: ' + (err.response?.data?.message || err.message));
    }
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!searchKeyword.trim()) {
      fetchTours(0);
      return;
    }

    try {
      setLoading(true);
      const response = await tourService.searchTours(searchKeyword, 0, 10);
      
      if (response.success && response.data) {
        setTours(response.data.content || []);
        setTotalPages(response.data.totalPages || 0);
        setCurrentPage(0);
      }
    } catch (err) {
      setError('Không thể tìm kiếm tour');
      console.error('Error searching tours:', err);
    } finally {
      setLoading(false);
    }
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND',
    }).format(price);
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString('vi-VN');
  };

  if (loading && tours.length === 0) {
    return <div className="loading">Đang tải...</div>;
  }

  return (
    <div className="tour-list-page">
      <div className="page-header">
        <h1>Quản lý Tour</h1>
        <Link to="/admin/tours/create" className="btn btn-primary">
          <i className="fas fa-plus"></i> Tạo tour mới
        </Link>
      </div>

      <div className="search-section">
        <form onSubmit={handleSearch} className="search-form">
          <input
            type="text"
            placeholder="Tìm kiếm tour theo tên, địa điểm..."
            value={searchKeyword}
            onChange={(e) => setSearchKeyword(e.target.value)}
            className="search-input"
          />
          <button type="submit" className="btn btn-search">
            <i className="fas fa-search"></i> Tìm kiếm
          </button>
          {searchKeyword && (
            <button
              type="button"
              onClick={() => {
                setSearchKeyword('');
                fetchTours(0);
              }}
              className="btn btn-secondary"
            >
              Xóa bộ lọc
            </button>
          )}
        </form>
      </div>

      {error && <div className="error-message">{error}</div>}

      <div className="table-container">
        <table className="tour-table">
          <thead>
            <tr>
              <th>#</th>
              <th>Tên Tour</th>
              <th>Địa điểm</th>
              <th>Mức độ</th>
              <th>Giá</th>
              <th>Ngày bắt đầu</th>
              <th>Ngày kết thúc</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            {tours.length === 0 ? (
              <tr>
                <td colSpan="8" className="no-data">
                  Không có tour nào
                </td>
              </tr>
            ) : (
              tours.map((tour, index) => (
                <tr key={tour.id}>
                  <td>{currentPage * 10 + index + 1}</td>
                  <td className="tour-name">{tour.name}</td>
                  <td>{tour.location || 'N/A'}</td>
                  <td>
                    <span className={`badge badge-${tour.level?.toLowerCase()}`}>
                      {tour.level}
                    </span>
                  </td>
                  <td className="tour-price">{formatPrice(tour.price)}</td>
                  <td>{formatDate(tour.startDate)}</td>
                  <td>{formatDate(tour.endDate)}</td>
                  <td className="actions">
                    <button
                      onClick={() => navigate(`/admin/tours/edit/${tour.id}`)}
                      className="btn btn-sm btn-warning"
                      title="Sửa"
                    >
                      <i className="fas fa-edit"></i>
                    </button>
                    <button
                      onClick={() => handleDelete(tour.id)}
                      className="btn btn-sm btn-danger"
                      title="Xóa"
                    >
                      <i className="fas fa-trash"></i>
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {totalPages > 1 && (
        <div className="pagination">
          <button
            onClick={() => fetchTours(currentPage - 1)}
            disabled={currentPage === 0}
            className="btn btn-secondary"
          >
            <i className="fas fa-chevron-left"></i> Trước
          </button>
          <span className="page-info">
            Trang {currentPage + 1} / {totalPages}
          </span>
          <button
            onClick={() => fetchTours(currentPage + 1)}
            disabled={currentPage >= totalPages - 1}
            className="btn btn-secondary"
          >
            Sau <i className="fas fa-chevron-right"></i>
          </button>
        </div>
      )}
    </div>
  );
};

export default TourList;
