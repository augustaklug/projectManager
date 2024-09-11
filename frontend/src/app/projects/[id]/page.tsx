// src/app/projects/[id]/page.tsx
"use client";

import { useAuth } from '@/hooks/useAuth';
import { AuthLayout } from '@/components/layout/AuthLayout'
import ProjectDetails from '@/components/projects/ProjectDetails';
import { useRouter } from 'next/navigation';
import { useEffect } from 'react';

interface ProjectDetailsPageProps {
  params: {
    id: string;
  };
}

export default function ProjectDetailsPage({ params }: ProjectDetailsPageProps) {
  const { isAuthenticated } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (isAuthenticated === false) {
      router.push('/login');
    }
  }, [isAuthenticated, router]);

  if (!isAuthenticated) return null;

  const projectId = parseInt(params.id);

  return (
    <AuthLayout>
      <div className="container mx-auto px-4 py-8">
        <h1 className="text-2xl font-bold mb-6">Project Details</h1>
        <ProjectDetails projectId={projectId} />
      </div>
    </AuthLayout>
  );
}