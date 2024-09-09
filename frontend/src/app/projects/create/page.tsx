"use client";

import { useAuth } from '@/hooks/useAuth';
import AuthLayout from '@/components/layout/AuthLayout';
import CreateProject from '@/components/projects/CreateProject';
import { useRouter } from 'next/navigation';
import { useEffect } from 'react';

export default function CreateProjectPage() {
  const { isAuthenticated } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (isAuthenticated === false) {
      router.push('/login');
    }
  }, [isAuthenticated, router]);

  if (!isAuthenticated) return null;

  console.log('Rendering CreateProjectPage');

  return (
    <AuthLayout>
      <div className="container mx-auto px-4 py-8">
        <h1 className="text-2xl font-bold mb-6">Create New Project</h1>
        <CreateProject />
      </div>
    </AuthLayout>
  );
}