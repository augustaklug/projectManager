// src/app/profile/page.tsx
"use client";

import { useAuth } from '@/hooks/useAuth';
import AuthLayout from '@/components/layout/AuthLayout';
import { useRouter } from 'next/navigation';
import { useEffect } from 'react';

export default function ProfilePage() {
  const { isAuthenticated } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (isAuthenticated === false) {
      router.push('/login');
    }
  }, [isAuthenticated, router]);

  if (!isAuthenticated) return null;

  return (
    <AuthLayout>
      <h1 className="text-2xl font-bold mb-4">Profile</h1>
      {/* Adicione aqui o conteúdo da página de perfil */}
    </AuthLayout>
  );
}