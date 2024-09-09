import api from '@/lib/api';
import {ProjectData} from '@/types/project';

export const projectService = {
    getAllProjects: async (): Promise<ProjectData[]> => {
        const response = await api.get('/projects');
        console.log("getAllProjects response:", response.data);
        return response.data;
    },

    getProjectById: async (id: number): Promise<ProjectData> => {
        console.log("Fetching project with ID:", id);
        const response = await api.get(`/projects/${id}`);
        console.log("getProjectById response:", response.data);
        return response.data;
    },

    getUserProjects: async (): Promise<ProjectData[]> => {
        const response = await api.get('/projects/user');
        console.log("getUserProjects response:", response.data);
        return response.data;
    },

    createProject: async (projectData: Omit<ProjectData, 'id' | 'tasks'>): Promise<ProjectData> => {
        console.log("Creating project with data:", projectData);
        const response = await api.post('/projects', projectData);
        console.log("createProject response:", response.data);
        return response.data;
    },

    updateProject: async (id: number, projectData: Partial<Omit<ProjectData, 'tasks'>>): Promise<ProjectData> => {
        console.log("Updating project with ID:", id, "and data:", projectData);
        const response = await api.put(`/projects/${id}`, projectData);
        console.log("updateProject response:", response.data);
        return response.data;
    },

    deleteProject: async (id: number): Promise<void> => {
        console.log("Deleting project with ID:", id);
        await api.delete(`/projects/${id}`);
        console.log("Project deleted successfully");
    },

    // New methods for handling notes
    addNoteToProject: async (projectId: number, noteData: { content: string }) => {
        const response = await api.post(`/projects/${projectId}/notes`, noteData);
        return response.data;
    },

    getProjectNotes: async (projectId: number) => {
        const response = await api.get(`/projects/${projectId}/notes`);
        return response.data;
    },

    updateProjectNote: async (projectId: number, noteId: number, noteData: { content: string }) => {
        const response = await api.put(`/projects/${projectId}/notes/${noteId}`, noteData);
        return response.data;
    },

    deleteProjectNote: async (noteId: number) => {
        await api.delete(`/projects/notes/${noteId}`);
    }
};