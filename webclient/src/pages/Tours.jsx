import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { tourService } from '../services/tourService';
import './Tours.css';

const Tours = () => {
  const [tours, setTours] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [searchKeyword, setSearchKeyword] = useState('');

  const fetchTours = async (page = 0, keyword = '') => {
    try {
      setLoading(true);
      setError(null);
      
      // Direct fetch to debug
      const url = `http://localhost:8081/api/tours?page=${page}&size=12`;
      console.log('Fetching:', url);
      
      const rawResponse = await fetch(url);
      console.log('Raw response status:', rawResponse.status);
      
      const data = await rawResponse.json();
      console.log('Raw data:', data);
      console.log('Data structure:', {
        hasStatus: 'status' in data,
        hasMessage: 'message' in data,
        hasData: 'data' in data,
        dataKeys: data.data ? Object.keys(data.data) : 'no data'
      });
      
      if (data && data.data && data.data.content) {
        console.log('Setting tours:', data.data.content);
        setTours(data.data.content);
        setTotalPages(data.data.totalPages || 0);
        setCurrentPage(page);
      } else {
        console.error('Unexpected structure:', data);
        setError('Cấu trúc dữ liệu không đúng');
      }
    } catch (err) {
      setError('Không thể tải danh sách tour: ' + (err.message || 'Unknown error'));
      console.error('Error fetching tours:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTours();
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    fetchTours(0, searchKeyword);
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
    return (
      <div className="tours-page">
        <div className="loading-container">
          <div className="spinner"></div>
          <p>Đang tải tours...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="tours-page">
      <section className="tours-hero">
        <div className="hero-content">
          <h1>Khám phá Tours</h1>
          <p>Tìm kiếm và đặt tour du lịch tuyệt vời khắp Việt Nam</p>
        </div>
      </section>

      <section className="tours-content">
        <div className="container">
          <div className="search-section">
            <form onSubmit={handleSearch} className="search-form">
              <div className="search-input-group">
                <i className="fas fa-search"></i>
                <input
                  type="text"
                  placeholder="Tìm kiếm tour theo tên, địa điểm..."
                  value={searchKeyword}
                  onChange={(e) => setSearchKeyword(e.target.value)}
                />
              </div>
              <button type="submit" className="btn btn-search">
                Tìm kiếm
              </button>
              {searchKeyword && (
                <button
                  type="button"
                  onClick={() => {
                    setSearchKeyword('');
                    fetchTours(0, '');
                  }}
                  className="btn btn-clear"
                >
                  Xóa
                </button>
              )}
            </form>
          </div>

          {error && <div className="error-message">{error}</div>}

          {tours.length === 0 ? (
            <div className="no-tours">
              <i className="fas fa-map-marked-alt"></i>
              <h3>Không tìm thấy tour nào</h3>
              <p>Thử tìm kiếm với từ khóa khác</p>
            </div>
          ) : (
            <>
              <div className="tours-grid">
                {tours.map((tour) => (
                  <div key={tour.id} className="tour-card">
                    <div className="tour-image">
                      {tour.images && tour.images.length > 0 ? (
                        <img src={tour.images[0]} alt={tour.name} />
                      ) : (
                        <div className="no-image">
                          <i className="fas fa-image"></i>
                        </div>
                      )}
                      {tour.category && (
                        <span className={`level-badge badge-easy`}>
                          {tour.category}
                        </span>
                      )}
                    </div>
                    
                    <div className="tour-info">
                      <h3 className="tour-name">{tour.name}</h3>
                      
                      <div className="tour-location">
                        <i className="fas fa-map-marker-alt"></i>
                        <span>{tour.province} - {tour.region}</span>
                      </div>
                      
                      <div className="tour-dates">
                        <div className="date-item">
                          <i className="fas fa-calendar-alt"></i>
                          <span>{formatDate(tour.startDate)}</span>
                        </div>
                        <span className="date-separator">→</span>
                        <div className="date-item">
                          <i className="fas fa-calendar-check"></i>
                          <span>{formatDate(tour.endDate)}</span>
                        </div>
                      </div>
                      
                      {tour.description && (
                        <p className="tour-description">
                          {tour.description.length > 100
                            ? `${tour.description.substring(0, 100)}...`
                            : tour.description}
                        </p>
                      )}
                      
                      <div className="tour-footer">
                        <div className="tour-price">
                          <span className="price-label">Giá từ</span>
                          <span className="price-value">
                            {tour.finalPrice ? formatPrice(tour.finalPrice) : formatPrice(tour.price)}
                          </span>
                          {tour.discount > 0 && (
                            <span className="discount-badge">-{tour.discount}%</span>
                          )}
                        </div>
                        <Link to={`/tours/${tour.id}`} className="btn btn-primary">
                          Xem chi tiết
                        </Link>
                      </div>
                    </div>
                  </div>
                ))}
              </div>

              {totalPages > 1 && (
                <div className="pagination">
                  <button
                    onClick={() => fetchTours(currentPage - 1, searchKeyword)}
                    disabled={currentPage === 0}
                    className="btn btn-pagination"
                  >
                    <i className="fas fa-chevron-left"></i> Trước
                  </button>
                  
                  <div className="page-numbers">
                    {[...Array(totalPages)].map((_, index) => (
                      <button
                        key={index}
                        onClick={() => fetchTours(index, searchKeyword)}
                        className={`btn btn-page ${currentPage === index ? 'active' : ''}`}
                      >
                        {index + 1}
                      </button>
                    ))}
                  </div>
                  
                  <button
                    onClick={() => fetchTours(currentPage + 1, searchKeyword)}
                    disabled={currentPage >= totalPages - 1}
                    className="btn btn-pagination"
                  >
                    Sau <i className="fas fa-chevron-right"></i>
                  </button>
                </div>
              )}
            </>
          )}
        </div>
      </section>
    </div>
  );
};

export default Tours;
