import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { tourService } from '../../services/tourService';
import './TourForm.css';

const TourForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEditMode = !!id;

  const [formData, setFormData] = useState({
    name: '',
    description: '',
    location: '',
    level: 'EASY',
    startDate: '',
    endDate: '',
    itinerary: '',
    price: '',
    maxParticipants: '',
    videoUrl: '',
    imageUrl: '',
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [imageFile, setImageFile] = useState(null);
  const [imagePreview, setImagePreview] = useState(null);

  useEffect(() => {
    if (isEditMode) {
      fetchTourData();
    }
  }, [id]);

  const fetchTourData = async () => {
    try {
      setLoading(true);
      const response = await tourService.getTourById(id);
      
      if (response.success && response.data) {
        const tour = response.data;
        setFormData({
          name: tour.name || '',
          description: tour.description || '',
          location: tour.location || '',
          level: tour.level || 'EASY',
          startDate: tour.startDate ? tour.startDate.split('T')[0] : '',
          endDate: tour.endDate ? tour.endDate.split('T')[0] : '',
          itinerary: tour.itinerary || '',
          price: tour.price || '',
          maxParticipants: tour.maxParticipants || '',
          videoUrl: tour.videoUrl || '',
          imageUrl: tour.imageUrl || '',
        });
        
        if (tour.imageUrl) {
          setImagePreview(tour.imageUrl);
        }
      }
    } catch (err) {
      setError('Không thể tải thông tin tour');
      console.error('Error fetching tour:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setImageFile(file);
      const reader = new FileReader();
      reader.onloadend = () => {
        setImagePreview(reader.result);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      let imageUrl = formData.imageUrl;

      // Upload image if new file selected
      if (imageFile) {
        const uploadResponse = await tourService.uploadTourImage(imageFile);
        if (uploadResponse.success && uploadResponse.data) {
          imageUrl = uploadResponse.data.url;
        }
      }

      const tourData = {
        ...formData,
        imageUrl,
        price: parseFloat(formData.price),
        maxParticipants: parseInt(formData.maxParticipants) || null,
      };

      if (isEditMode) {
        await tourService.updateTour(id, tourData);
        alert('Cập nhật tour thành công!');
      } else {
        await tourService.createTour(tourData);
        alert('Tạo tour mới thành công!');
      }

      navigate('/admin/tours');
    } catch (err) {
      setError(err.response?.data?.message || 'Có lỗi xảy ra khi lưu tour');
      console.error('Error saving tour:', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading && isEditMode && !formData.name) {
    return <div className="loading">Đang tải...</div>;
  }

  return (
    <div className="tour-form-page">
      <div className="form-header">
        <h1>{isEditMode ? 'Chỉnh sửa Tour' : 'Tạo Tour Mới'}</h1>
        <button onClick={() => navigate('/admin/tours')} className="btn btn-secondary">
          <i className="fas fa-arrow-left"></i> Quay lại
        </button>
      </div>

      {error && <div className="error-message">{error}</div>}

      <form onSubmit={handleSubmit} className="tour-form">
        <div className="form-grid">
          <div className="form-group">
            <label htmlFor="name">Tên Tour *</label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
              placeholder="Nhập tên tour"
            />
          </div>

          <div className="form-group">
            <label htmlFor="location">Địa điểm *</label>
            <input
              type="text"
              id="location"
              name="location"
              value={formData.location}
              onChange={handleChange}
              required
              placeholder="Nhập địa điểm"
            />
          </div>

          <div className="form-group">
            <label htmlFor="level">Mức độ *</label>
            <select
              id="level"
              name="level"
              value={formData.level}
              onChange={handleChange}
              required
            >
              <option value="EASY">Dễ</option>
              <option value="MEDIUM">Vừa</option>
              <option value="HARD">Khó</option>
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="price">Giá Tour (VNĐ) *</label>
            <input
              type="number"
              id="price"
              name="price"
              value={formData.price}
              onChange={handleChange}
              required
              min="0"
              placeholder="Nhập giá tour"
            />
          </div>

          <div className="form-group">
            <label htmlFor="startDate">Ngày bắt đầu *</label>
            <input
              type="date"
              id="startDate"
              name="startDate"
              value={formData.startDate}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="endDate">Ngày kết thúc *</label>
            <input
              type="date"
              id="endDate"
              name="endDate"
              value={formData.endDate}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="maxParticipants">Số người tối đa</label>
            <input
              type="number"
              id="maxParticipants"
              name="maxParticipants"
              value={formData.maxParticipants}
              onChange={handleChange}
              min="1"
              placeholder="Nhập số người tối đa"
            />
          </div>

          <div className="form-group">
            <label htmlFor="videoUrl">URL Video YouTube</label>
            <input
              type="url"
              id="videoUrl"
              name="videoUrl"
              value={formData.videoUrl}
              onChange={handleChange}
              placeholder="https://youtube.com/..."
            />
          </div>
        </div>

        <div className="form-group full-width">
          <label htmlFor="description">Mô tả Tour</label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleChange}
            rows="4"
            placeholder="Nhập mô tả chi tiết về tour"
          />
        </div>

        <div className="form-group full-width">
          <label htmlFor="itinerary">Lịch trình</label>
          <textarea
            id="itinerary"
            name="itinerary"
            value={formData.itinerary}
            onChange={handleChange}
            rows="6"
            placeholder="Nhập lịch trình chi tiết"
          />
        </div>

        <div className="form-group full-width">
          <label htmlFor="image">Hình ảnh Tour</label>
          <input
            type="file"
            id="image"
            accept="image/*"
            onChange={handleImageChange}
          />
          {imagePreview && (
            <div className="image-preview">
              <img src={imagePreview} alt="Preview" />
            </div>
          )}
        </div>

        <div className="form-actions">
          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? (
              <>
                <i className="fas fa-spinner fa-spin"></i> Đang lưu...
              </>
            ) : (
              <>
                <i className="fas fa-save"></i> {isEditMode ? 'Cập nhật' : 'Tạo mới'}
              </>
            )}
          </button>
          <button
            type="button"
            onClick={() => navigate('/admin/tours')}
            className="btn btn-secondary"
            disabled={loading}
          >
            Hủy
          </button>
        </div>
      </form>
    </div>
  );
};

export default TourForm;
