import React, { useEffect, useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { projectService } from '@/services/projectService';
import { Progress } from "@/components/ui/progress"
import Link from 'next/link';
import { ProjectData } from '@/types/project';
import { Loader2, Calendar, ArrowRight, CheckCircle2, AlertCircle } from "lucide-react"
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert"

const ProjectList: React.FC = () => {
  const [projects, setProjects] = useState<ProjectData[]>([]);
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

  const getProjectStatus = (project: ProjectData) => {
    if (!project.tasks || project.tasks.length === 0) {
      return 'No tasks';
    }
    const completedTasks = project.tasks.filter(task => task.status === 'Completed').length;
    const totalTasks = project.tasks.length;
    const percentComplete = (completedTasks / totalTasks) * 100;
    return `${percentComplete.toFixed(0)}% Complete`;
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <Loader2 className="h-8 w-8 animate-spin text-primary" />
      </div>
    );
  }

  if (error) {
    return (
      <Alert variant="destructive">
        <AlertCircle className="h-4 w-4" />
        <AlertTitle>Error</AlertTitle>
        <AlertDescription>{error}</AlertDescription>
      </Alert>
    );
  }

  return (
    <div className="space-y-6">
      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
        {projects.map((project) => (
          <Card key={project.id} className="flex flex-col">
            <CardHeader>
              <CardTitle className="text-xl">{project.name}</CardTitle>
              <CardDescription className="flex items-center text-muted-foreground">
                <Calendar className="mr-2 h-4 w-4" />
                {new Date(project.startDate).toLocaleDateString()} - {new Date(project.endDate).toLocaleDateString()}
              </CardDescription>
            </CardHeader>
            <CardContent className="flex-grow">
              <div className="mb-4">
                <div className="flex justify-between items-center mb-2">
                  <span className="text-sm font-medium text-foreground">Progress</span>
                  <span className="text-sm font-medium text-foreground">{getProjectStatus(project)}</span>
                </div>
                {project.tasks && project.tasks.length > 0 ? (
                  <Progress
                    value={(project.tasks.filter(task => task.status === 'Completed').length / project.tasks.length) * 100}
                    className="h-2"
                  />
                ) : (
                  <p className="text-sm text-muted-foreground">No tasks added yet</p>
                )}
              </div>
              <div className="flex justify-between items-center">
                <div className="flex items-center">
                  <CheckCircle2 className="mr-2 h-4 w-4 text-green-500" />
                  <span className="text-sm text-muted-foreground">
                    {project.tasks?.filter(task => task.status === 'Completed').length || 0} completed
                  </span>
                </div>
                <div className="flex items-center">
                  <AlertCircle className="mr-2 h-4 w-4 text-yellow-500" />
                  <span className="text-sm text-muted-foreground">
                    {project.tasks?.filter(task => task.status !== 'Completed').length || 0} pending
                  </span>
                </div>
              </div>
            </CardContent>
            <CardContent className="pt-0">
              <Button asChild className="w-full">
                <Link href={`/projects/${project.id}`}>
                  View Details
                  <ArrowRight className="ml-2 h-4 w-4" />
                </Link>
              </Button>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
};

export default ProjectList;