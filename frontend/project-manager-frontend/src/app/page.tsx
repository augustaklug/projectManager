// src/app/page.tsx
"use client";

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/hooks/useAuth';
import LoginForm from '@/components/auth/LoginForm';
import Dashboard from '@/components/dashboard/Dashboard';

export default function Home() {
  const { isAuthenticated } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (isAuthenticated === false) {
      router.push('/login');
    } else if (isAuthenticated === true) {
      router.push('/dashboard');
    }
  }, [isAuthenticated, router]);

  // Enquanto verifica a autenticação, pode mostrar um loader
  if (isAuthenticated === null) {
    return <div>Loading...</div>;
  }

  // Este retorno nunca deve ser renderizado devido ao redirecionamento,
  // mas é necessário para satisfazer o TypeScript/React
  return null;
}