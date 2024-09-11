import axios from 'axios';
import {authService} from '@/services/authService';

// Use environment variable for base URL, defaulting to the Docker service name
const API_URL = process.env.NEXT_PUBLIC_API_URL || 'https://angularengenharia.com/api';

const api = axios.create({
    baseURL: API_URL,
});

// Interceptor para adicionar o token JWT a todas as requisições
api.interceptors.request.use((config) => {
    const token = authService.getToken();
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
}, (error) => {
    return Promise.reject(error);
});

// Interceptor para lidar com erros de resposta
api.interceptors.response.use(
    (response) => response,
    (error) => {
        return Promise.reject(error);
    }
);

export default api;