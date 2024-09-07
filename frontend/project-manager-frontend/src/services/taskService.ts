import api from '@/lib/api';
import {TaskData} from '@/types/task';

// export interface TaskData {
//     name: string;
//     description?: string;
//     status: string;
//     deadline?: string;
//     projectId: number;
//     assignedToId?: number;
// }

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

    createTask: async (taskData: TaskData) => {
        const response = await api.post('/tasks', taskData);
        return response.data;
    },

    updateTask: async (id: number, taskData: Partial<TaskData>) => {
        const response = await api.put(`/tasks/${id}`, taskData);
        return response.data;
    },

    deleteTask: async (id: number) => {
        await api.delete(`/tasks/${id}`);
    },

    // New methods for handling notes
    addNoteToTask: async (taskId: number, noteData: { content: string }) => {
        const response = await api.post(`/tasks/${taskId}/notes`, noteData);
        return response.data;
    },

    getTaskNotes: async (taskId: number) => {
        const response = await api.get(`/tasks/${taskId}/notes`);
        return response.data;
    },

    updateTaskNote: async (taskId: number, noteId: number, noteData: { content: string }) => {
        const response = await api.put(`/tasks/${taskId}/notes/${noteId}`, noteData);
        return response.data;
    },

    deleteTaskNote: async (noteId: number) => {
        await api.delete(`/tasks/notes/${noteId}`);
    }
};