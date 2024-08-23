import api from '@/lib/api';

export const authService = {
  login: async (username: string, password: string) => {
    try {
      const response = await api.post('/auth/signin', { username, password });
      if (response.data && response.data.token) {
        if (typeof window !== 'undefined') {
          localStorage.setItem('token', response.data.token);
        }
        return response.data;
      } else {
        throw new Error('Invalid response from server');
      }
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    }
  },

  register: async (username: string, email: string, password: string) => {
    try {
      const response = await api.post('/auth/signup', { username, email, password });
      return response.data;
    } catch (error) {
      console.error('Registration error:', error);
      throw error;
    }
  },

  logout: () => {
    if (typeof window !== 'undefined') {
      localStorage.removeItem('token');
    }
  },

  getToken: () => {
    if (typeof window !== 'undefined') {
      return localStorage.getItem('token');
    }
    return null;
  }
};