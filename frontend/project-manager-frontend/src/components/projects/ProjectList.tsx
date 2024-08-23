import React, { useEffect, useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { projectService } from '@/services/projectService';
import { Progress } from "@/components/ui/progress"
import Link from 'next/link';

interface Project {
  id: number;
  name: string;
  startDate: string;
  endDate: string;
  tasks?: { id: number; status: string }[];
}

const ProjectList = () => {
  const [projects, setProjects] = useState<Project[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchProjects = async () => {
      try {
        const data = await projectService.getUserProjects();
        setProjects(data);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch projects');
        setLoading(false);
      }
    };

    fetchProjects();
  }, []);

  const getProjectStatus = (project: Project) => {
    if (!project.tasks || project.tasks.length === 0) {
      return 'No tasks';
    }
    const completedTasks = project.tasks.filter(task => task.status === 'Completed').length;
    const totalTasks = project.tasks.length;
    const percentComplete = (completedTasks / totalTasks) * 100;
    return `${percentComplete.toFixed(0)}% Complete`;
  };

  if (loading) return <p>Loading projects...</p>;
  if (error) return <p>Error: {error}</p>;

  return (
    <div className="space-y-4">
      <h2 className="text-2xl font-bold">Projects</h2>
      {projects.map((project) => (
        <Card key={project.id}>
          <CardHeader>
            <CardTitle>{project.name}</CardTitle>
          </CardHeader>
          <CardContent>
            <p>Start Date: {new Date(project.startDate).toLocaleDateString()}</p>
            <p>End Date: {new Date(project.endDate).toLocaleDateString()}</p>
            <p>Status: {getProjectStatus(project)}</p>
            {project.tasks && project.tasks.length > 0 && (
              <Progress value={(project.tasks.filter(task => task.status === 'Completed').length / project.tasks.length) * 100} className="mt-2" />
            )}
            <Link href={`/projects/${project.id}`} passHref>
              <Button className="mt-2">View Details</Button>
            </Link>
          </CardContent>
        </Card>
      ))}
    </div>
  );
};

export default ProjectList;