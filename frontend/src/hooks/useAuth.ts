import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { authService } from '@/services/authService';

export const useAuth = () => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);
  const [userId, setUserId] = useState<string | null>(null);
  const router = useRouter();

  useEffect(() => {
    const checkAuth = async () => {
      const token = authService.getToken();
      const storedUserId = authService.getUsername();
      if (token && storedUserId) {
        try {
          setIsAuthenticated(true);
          setUserId(storedUserId);
        } catch (error) {
          console.error('Token verification failed:', error);
          setIsAuthenticated(false);
          setUserId(null);
          authService.logout(); // Limpa o token inválido
        }
      } else {
        setIsAuthenticated(false);
        setUserId(null);
      }
    };

    checkAuth();
  }, []);

  const login = async (username: string, password: string) => {
    try {
      await authService.login(username, password);
      setIsAuthenticated(true);
      setUserId(authService.getUsername());
      router.push('/dashboard');
    } catch (error) {
      console.error('Login error:', error);
      throw error; // Repassar o erro para ser tratado no componente
    }
  };

  const logout = () => {
    authService.logout();
    setIsAuthenticated(false);
    setUserId(null);
    router.push('/');
  };

  return { isAuthenticated, userId, login, logout };
};