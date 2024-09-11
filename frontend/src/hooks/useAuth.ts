import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { authService } from '@/services/authService';

export const useAuth = () => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);
  const [userId, setUserId] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    const checkAuth = async () => {
      setIsLoading(true);
      const token = authService.getToken();
      if (!token) {
        console.log('No token found, redirecting to login');
        setIsAuthenticated(false);
        setUserId(null);
        router.push('/login');
        setIsLoading(false);
        return;
      }

      const storedUserId = authService.getUsername();
      if (storedUserId) {
        try {
          // Here you might want to add a token verification step if needed
          // For example: await authService.verifyToken(token);
          setIsAuthenticated(true);
          setUserId(storedUserId);
        } catch (error) {
          console.error('Token verification failed:', error);
          handleAuthFailure();
        }
      } else {
        handleAuthFailure();
      }
      setIsLoading(false);
    };

    checkAuth();
  }, [router]);

  const handleAuthFailure = () => {
    setIsAuthenticated(false);
    setUserId(null);
    authService.logout(); // Clear the invalid token
    router.push('/login');
  };

  const login = async (username: string, password: string) => {
    try {
      await authService.login(username, password);
      const userId = authService.getUsername();
      if (userId) {
        setIsAuthenticated(true);
        setUserId(userId);
        router.push('/dashboard');
      } else {
        throw new Error('Failed to get user ID after login');
      }
    } catch (error) {
      console.error('Login error:', error);
      throw error; // Pass the error to be handled in the component
    }
  };

  const logout = () => {
    authService.logout();
    setIsAuthenticated(false);
    setUserId(null);
    router.push('/login');
  };

  return { isAuthenticated, userId, isLoading, login, logout };
};