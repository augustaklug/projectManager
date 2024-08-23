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

    updateCurrentUser: async (userData: Partial<UserData>): Promise<UserData> => {
        const currentUserId = authService.getUsername();
        if (!currentUserId) {
            throw new Error('User not authenticated');
        }
        try {
            const response = await api.put(`/users/${currentUserId}`, userData);
            return response.data;
        } catch (error) {
            console.error('Error updating current user:', error);
            throw error;
        }
    },

    // Note: The changePassword method is not available in the current UserController.
};