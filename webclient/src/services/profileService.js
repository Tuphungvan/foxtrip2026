import api from '../config/api';

export const profileService = {
  // Lấy profile
  getProfile: async () => {
    return await api.get('/profile');
  },

  // Cập nhật profile
  updateProfile: async (profileData) => {
    return await api.put('/profile', profileData);
  },
};
