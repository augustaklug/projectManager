import api from '@/lib/api';
import { NoteData } from '@/types/note';

export const noteService = {
  createNote: async (note: NoteData): Promise<NoteData> => {
    const response = await api.post('/api/notes', note);
    return response.data;
  },

  updateNote: async (id: number, note: Partial<NoteData>): Promise<NoteData> => {
    const response = await api.put(`/api/notes/${id}`, note);
    return response.data;
  },

  deleteNote: async (id: number): Promise<void> => {
    await api.delete(`/api/notes/${id}`);
  },

  getNoteById: async (id: number): Promise<NoteData> => {
    const response = await api.get(`/api/notes/${id}`);
    return response.data;
  },

  getNotesByTaskId: async (taskId: number): Promise<NoteData[]> => {
    const response = await api.get(`/api/tasks/${taskId}/notes`);
    return response.data;
  },

  getNotesByProjectId: async (projectId: number): Promise<NoteData[]> => {
    const response = await api.get(`/api/projects/${projectId}/notes`);
    return response.data;
  }
};