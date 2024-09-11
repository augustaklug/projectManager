// src/app/tasks/page.tsx
"use client";

import { useAuth } from '@/hooks/useAuth';
import { AuthLayout } from '@/components/layout/AuthLayout'
import TaskList from '@/components/tasks/TaskList';
import { useRouter } from 'next/navigation';
import { useEffect } from 'react';

export default function TasksPage() {
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
      <TaskList />
    </AuthLayout>
  );
}