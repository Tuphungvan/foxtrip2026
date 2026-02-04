import api from '../config/api';

export const tourService = {
  // Get all tours with pagination
  getAllTours: async (page = 0, size = 10, sort = 'createdAt,desc') => {
    const response = await api.get('/tours', {
      params: { page, size, sort }
    });
    return response.data;
  },

  // Get tour by ID
  getTourById: async (id) => {
    const response = await api.get(`/tours/${id}`);
    return response.data;
  },

  // Create new tour
  createTour: async (tourData) => {
    const response = await api.post('/tours', tourData);
    return response.data;
  },

  // Update tour
  updateTour: async (id, tourData) => {
    const response = await api.put(`/tours/${id}`, tourData);
    return response.data;
  },

  // Delete tour
  deleteTour: async (id) => {
    const response = await api.delete(`/tours/${id}`);
    return response.data;
  },

  // Search tours
  searchTours: async (keyword, page = 0, size = 10) => {
    const response = await api.get('/tours/search', {
      params: { keyword, page, size }
    });
    return response.data;
  },

  // Upload tour image
  uploadTourImage: async (file) => {
    const formData = new FormData();
    formData.append('file', file);
    const response = await api.post('/cloudinary/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  },
};
