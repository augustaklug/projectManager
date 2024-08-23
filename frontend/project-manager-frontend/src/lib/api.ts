import axios from 'axios';
import { authService } from '@/services/authService';

const api = axios.create({
  baseURL: 'http://localhost:8080/api', // Ajuste conforme necessário
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
    if (error.response && error.response.status === 401) {
      // Token expirado ou inválido, faça logout
      authService.logout();
      // Redirecione para a página de login ou dispare um evento de logout
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;