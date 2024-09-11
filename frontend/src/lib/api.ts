import axios from 'axios';
import {authService} from '@/services/authService';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'https://angularengenharia.com/api';

const api = axios.create({
    baseURL: API_URL,
});

api.interceptors.request.use((config) => {
    const token = authService.getToken();
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
}, (error) => {
    return Promise.reject(error);
});

api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response && error.response.status === 401) {
            // Token expirado ou inválido
            authService.logout();
            window.location.href = '/login'; // Redireciona para a página de login
        }
        return Promise.reject(error);
    }
);

export default api;