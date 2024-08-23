import api from '@/lib/api';

export const taskService = {
  getAllTasks: async () => {
    const response = await api.get('/tasks');
    return response.data;
  },

  getTaskById: async (id: number) => {
    const response = await api.get(`/tasks/${id}`);
    return response.data;
  },

  getUserTasks: async () => {
    const response = await api.get('/tasks/user');
    return response.data;
  },

  createTask: async (taskData: any) => {
    const response = await api.post('/tasks', taskData);
    return response.data;
  },

  updateTask: async (id: number, taskData: any) => {
    const response = await api.put(`/tasks/${id}`, taskData);
    return response.data;
  },

  deleteTask: async (id: number) => {
    await api.delete(`/tasks/${id}`);
  }
};