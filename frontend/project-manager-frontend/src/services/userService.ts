import api from '@/lib/api';

export interface UserData {
  id?: number;
  username: string;
  email: string;
  role?: string;
}

export const userService = {
  getAllUsers: async (): Promise<UserData[]> => {
    const response = await api.get('/users');
    return response.data;
  },

  getUserById: async (id: number): Promise<UserData> => {
    const response = await api.get(`/users/${id}`);
    return response.data;
  },

  createUser: async (userData: Omit<UserData, 'id'>): Promise<UserData> => {
    const response = await api.post('/users', userData);
    return response.data;
  },

  updateUser: async (id: number, userData: Partial<UserData>): Promise<UserData> => {
    const response = await api.put(`/users/${id}`, userData);
    return response.data;
  },

  deleteUser: async (id: number): Promise<void> => {
    await api.delete(`/users/${id}`);
  },

  getCurrentUser: async (): Promise<UserData> => {
    const response = await api.get('/users/me');
    return response.data;
  }
};