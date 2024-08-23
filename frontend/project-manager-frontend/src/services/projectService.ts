import api from '@/lib/api';

export interface ProjectData {
  name: string;
  startDate: string;
  endDate: string;
  teamMemberIds: number[];
}

export const projectService = {
  getAllProjects: async () => {
    const response = await api.get('/projects');
    return response.data;
  },

  getProjectById: async (id: number) => {
    const response = await api.get(`/projects/${id}`);
    return response.data;
  },

  getUserProjects: async () => {
    const response = await api.get('/projects/user');
    return response.data;
  },

  createProject: async (projectData: ProjectData) => {
    const response = await api.post('/projects', projectData);
    return response.data;
  },

  updateProject: async (id: number, projectData: Partial<ProjectData>) => {
    const response = await api.put(`/projects/${id}`, projectData);
    return response.data;
  },

  deleteProject: async (id: number) => {
    await api.delete(`/projects/${id}`);
  }
};