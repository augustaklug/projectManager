// src/app/dashboard/page.tsx
"use client";

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/hooks/useAuth';
import Dashboard from '@/components/dashboard/Dashboard';
import AuthLayout from '@/components/layout/AuthLayout';

export default function DashboardPage() {
  const { isAuthenticated } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (isAuthenticated === false) {
      router.push('/login');
    }
  }, [isAuthenticated, router]);

  if (isAuthenticated === null) {
    return <div>Loading...</div>;
  }

  if (isAuthenticated === false) {
    return null; // Isso nunca deve ser renderizado devido ao redirecionamento
  }

  return (
    <AuthLayout>
      <Dashboard />
    </AuthLayout>
  );
}