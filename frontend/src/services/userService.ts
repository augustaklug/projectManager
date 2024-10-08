import api from '@/lib/api';
import {authService} from "@/services/authService";

export interface UserData {
    id?: number;
    username: string;
    email: string;
    role?: string;
}

export const userService = {
    getAllUsers: async (): Promise<UserData[]> => {
        try {
            const response = await api.get('/users');
            return response.data;
        } catch (error) {
            console.error('Error fetching all users:', error);
            throw error;
        }
    },

    getUserByUsername: async (username: string): Promise<UserData> => {
        try {
            const response = await api.get(`/users/username/${username}`);
            return response.data;
        } catch (error: any) {
            console.error(`Error fetching user with username ${username}:`, error.response?.data || error.message);
            throw error;
        }
    },

    getUserById: async (id: number): Promise<UserData> => {
        try {
            const response = await api.get(`/users/id/${id}`);
            return response.data;
        } catch (error: any) {
            console.error(`Error fetching user with id ${id}:`, error.response?.data || error.message);
            throw error;
        }
    },

    getCurrentUser: async (): Promise<UserData> => {
        const currentUsername = authService.getUsername();
        if (!currentUsername) {
            console.error('getCurrentUser: No username found');
            throw new Error('User not authenticated');
        }

        try {
            return await userService.getUserByUsername(currentUsername);
        } catch (error: any) {
            console.error('getCurrentUser: Error fetching current user:', error.response?.data || error.message);
            throw error;
        }
    },

    updateUser: async (username: string, userData: Partial<UserData>): Promise<UserData> => {
        try {
            const response = await api.put(`/users/${username}`, userData);
            return response.data;
        } catch (error) {
            console.error('Error updating user:', error);
            throw error;
        }
    },

    deleteUser: async (username: string): Promise<void> => {
        try {
            await api.delete(`/users/${username}`);
        } catch (error) {
            console.error('Error deleting user:', error);
            throw error;
        }
    },

    getUserHistory: async (username: string): Promise<any[]> => {
        try {
            const response = await api.get(`/users/${username}/history`);
            return response.data;
        } catch (error) {
            console.error('Error fetching user history:', error);
            throw error;
        }
    },
};

export default userService;