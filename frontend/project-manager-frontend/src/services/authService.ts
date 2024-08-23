import api from '@/lib/api';
import {jwtDecode} from "jwt-decode";

interface DecodedToken {
    sub: string;  // Assuming the user ID is stored in the 'sub' claim
    // Add other claims as needed
}

export const authService = {
    login: async (username: string, password: string) => {
        try {
            const response = await api.post('/auth/signin', {username, password});
            if (response.data && response.data.token) {
                localStorage.setItem('token', response.data.token);
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
            const response = await api.post('/auth/signup', {username, email, password});
            return response.data;
        } catch (error) {
            console.error('Registration error:', error);
            throw error;
        }
    },

    logout: () => {
        localStorage.removeItem('token');
    },

    getToken: () => {
        const token = localStorage.getItem('token');
        return token;
    },

    getUsername: (): string | null => {
        const token = authService.getToken();
        if (!token) {
            console.log('No token found');
            return null;
        }

        try {
            const decodedToken = jwtDecode<DecodedToken>(token);
            return decodedToken.sub;  // This is returning the username
        } catch (error) {
            console.error('Error decoding token:', error);
            return null;
        }
    },

};