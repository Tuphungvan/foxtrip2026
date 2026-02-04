import api from '../config/api';

export const authService = {
  // Đăng nhập
  login: async (username, password) => {
    // api.post returns ApiResponse after interceptor unwraps axios response
    // ApiResponse structure: { success, message, data: LoginResponse }
    const apiResponse = await api.post('/auth/login', { username, password });
    
    if (apiResponse.data) {
      // Backend now returns camelCase (LoginResponse DTO)
      const { accessToken, refreshToken } = apiResponse.data;
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
    }
    return apiResponse;
  },

  // Đăng ký
  register: async (userData) => {
    return await api.post('/auth/register', userData);
  },

  // Đăng xuất
  logout: async () => {
    const refreshToken = localStorage.getItem('refreshToken');
    try {
      await api.post('/auth/logout', refreshToken, {
        headers: { 'Content-Type': 'text/plain' }
      });
    } finally {
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
    }
  },

  // Lấy thông tin user hiện tại
  getCurrentUser: async () => {
    // Returns ApiResponse<UserProfile>
    return await api.get('/auth/me');
  },

  // Verify token
  verifyToken: async () => {
    return await api.get('/auth/verify');
  },

  // Check if logged in
  isAuthenticated: () => {
    return !!localStorage.getItem('accessToken');
  },
};
