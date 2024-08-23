// src/app/projects/[id]/page.tsx
"use client";

import { useEffect } from 'react';
import { useAuth } from '@/hooks/useAuth';
import AuthLayout from '@/components/layout/AuthLayout';
import ProjectDetails from '@/components/projects/ProjectDetails';
import { useRouter } from 'next/navigation';

interface ProjectDetailsPageProps {
  params: {
    id: string;
  };
}

export default function ProjectDetailsPage({ params }: ProjectDetailsPageProps) {
  const { isAuthenticated } = useAuth();
  const router = useRouter();
  const projectId = parseInt(params.id, 10);

  useEffect(() => {
    if (isAuthenticated === false) {
      router.push('/login');
    }
  }, [isAuthenticated, router]);

  if (!isAuthenticated) return null;

  return (
    <AuthLayout>
      <ProjectDetails projectId={projectId} />
    </AuthLayout>
  );
}