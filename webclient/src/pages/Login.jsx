import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import './Auth.css';

const Login = () => {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const userData = await login(formData.username, formData.password);
      
      console.log('=== LOGIN DEBUG ===');
      console.log('User data after login:', userData);
      console.log('isAdmin:', userData?.isAdmin);
      console.log('isSuperAdmin:', userData?.isSuperAdmin);
      console.log('roles:', userData?.roles);
      
      // Alert để debug
      alert(`Login success!\nisAdmin: ${userData?.isAdmin}\nisSuperAdmin: ${userData?.isSuperAdmin}\nRoles: ${userData?.roles?.join(', ')}`);
      
      // Redirect based on user role
      if (userData && (userData.isAdmin === true || userData.isSuperAdmin === true)) {
        console.log('✅ Redirecting to /admin');
        alert('Redirecting to /admin');
        navigate('/admin');
      } else {
        console.log('✅ Redirecting to /');
        alert('Redirecting to /');
        navigate('/');
      }
    } catch (err) {
      console.error('Login error:', err);
      const errorMessage = err.response?.data?.message || err.message || 'Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.';
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-container">
        <div className="auth-card">
          <h2 className="auth-title">Đăng nhập</h2>
          
          {error && (
            <div className="alert alert-error">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="auth-form">
            <div className="form-group">
              <input
                type="text"
                name="username"
                className="form-input"
                placeholder="Tên đăng nhập"
                value={formData.username}
                onChange={handleChange}
                required
                disabled={loading}
              />
            </div>

            <div className="form-group">
              <input
                type="password"
                name="password"
                className="form-input"
                placeholder="Mật khẩu"
                value={formData.password}
                onChange={handleChange}
                required
                disabled={loading}
              />
            </div>

            <button 
              type="submit" 
              className="btn btn-primary btn-block"
              disabled={loading}
            >
              {loading ? 'Đang đăng nhập...' : 'Đăng nhập'}
            </button>
          </form>

          <p className="auth-footer">
            Bạn chưa có tài khoản? <Link to="/register">Đăng ký ngay</Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Login;
