import {useState, useEffect, useCallback} from 'react';
import {useRouter} from 'next/navigation';
import {authService} from '@/services/authService';

export const useAuth = () => {
    const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);
    const [userId, setUserId] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const router = useRouter();

    const handleAuthFailure = useCallback(() => {
        setIsAuthenticated(false);
        setUserId(null);
        authService.logout();
    }, []);

    const checkAuth = useCallback(async () => {
        setIsLoading(true);
        const token = authService.getToken();
        if (!token) {
            setIsAuthenticated(false);
            setUserId(null);
            setIsLoading(false);
            return;
        }

        try {
            await authService.verifyToken();
            const storedUserId = authService.getUsername();
            setIsAuthenticated(true);
            setUserId(storedUserId);
        } catch (error) {
            console.error('Token verification failed:', error);
            handleAuthFailure();
        }
        setIsLoading(false);
    }, [handleAuthFailure]);

    useEffect(() => {
        checkAuth();
    }, [checkAuth]);

    const login = async (username: string, password: string) => {
        try {
            await authService.login(username, password);
            await checkAuth();
            router.push('/dashboard');
        } catch (error) {
            console.error('Login error:', error);
            throw error;
        }
    };

    const logout = useCallback(() => {
        authService.logout();
        setIsAuthenticated(false);
        setUserId(null);
        router.push('/login');
    }, [router]);

    return {isAuthenticated, userId, isLoading, login, logout, checkAuth};
};