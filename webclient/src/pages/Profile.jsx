import { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { profileService } from '../services/profileService';
import './Profile.css';

const Profile = () => {
  const { user } = useAuth();
  const [formData, setFormData] = useState({
    fullName: '',
    phoneNumber: '',
    address: '',
    dateOfBirth: '',
    gender: '',
  });
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState({ type: '', text: '' });

  useEffect(() => {
    if (user) {
      setFormData({
        fullName: user.fullName || '',
        phoneNumber: user.phoneNumber || '',
        address: user.address || '',
        dateOfBirth: user.dateOfBirth || '',
        gender: user.gender || '',
      });
    }
  }, [user]);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
    setMessage({ type: '', text: '' });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage({ type: '', text: '' });

    try {
      await profileService.updateProfile(formData);
      setMessage({ 
        type: 'success', 
        text: 'Cập nhật thông tin thành công!' 
      });
    } catch (err) {
      const errorMessage = err.response?.data?.message || err.message || 'Cập nhật thất bại. Vui lòng thử lại.';
      setMessage({ 
        type: 'error', 
        text: errorMessage
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="profile-page">
      <div className="profile-container">
        <div className="profile-header">
          <h1>Thông tin cá nhân</h1>
          <p>Quản lý thông tin của bạn</p>
        </div>

        <div className="profile-card">
          <div className="profile-info">
            <div className="avatar-section">
              <div className="avatar">
                {user?.avatar ? (
                  <img src={user.avatar} alt={user.fullName} />
                ) : (
                  <i className="fas fa-user"></i>
                )}
              </div>
              <div className="user-basic-info">
                <h3>{user?.fullName || user?.username}</h3>
                <p>{user?.email}</p>
              </div>
            </div>
          </div>

          {message.text && (
            <div className={`alert alert-${message.type}`}>
              {message.text}
            </div>
          )}

          <form onSubmit={handleSubmit} className="profile-form">
            <div className="form-group">
              <label className="form-label">Họ và tên</label>
              <input
                type="text"
                name="fullName"
                className="form-input"
                value={formData.fullName}
                onChange={handleChange}
                disabled={loading}
              />
            </div>

            <div className="form-group">
              <label className="form-label">Số điện thoại</label>
              <input
                type="tel"
                name="phoneNumber"
                className="form-input"
                value={formData.phoneNumber}
                onChange={handleChange}
                disabled={loading}
              />
            </div>

            <div className="form-group">
              <label className="form-label">Địa chỉ</label>
              <textarea
                name="address"
                className="form-input"
                rows="3"
                value={formData.address}
                onChange={handleChange}
                disabled={loading}
              />
            </div>

            <div className="form-row">
              <div className="form-group">
                <label className="form-label">Ngày sinh</label>
                <input
                  type="date"
                  name="dateOfBirth"
                  className="form-input"
                  value={formData.dateOfBirth}
                  onChange={handleChange}
                  disabled={loading}
                />
              </div>

              <div className="form-group">
                <label className="form-label">Giới tính</label>
                <select
                  name="gender"
                  className="form-input"
                  value={formData.gender}
                  onChange={handleChange}
                  disabled={loading}
                >
                  <option value="">Chọn giới tính</option>
                  <option value="male">Nam</option>
                  <option value="female">Nữ</option>
                  <option value="other">Khác</option>
                </select>
              </div>
            </div>

            <button 
              type="submit" 
              className="btn btn-primary"
              disabled={loading}
            >
              {loading ? 'Đang cập nhật...' : 'Cập nhật thông tin'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Profile;
