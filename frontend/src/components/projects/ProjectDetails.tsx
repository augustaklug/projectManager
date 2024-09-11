import React, { useState, useEffect, useCallback } from 'react';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Progress } from "@/components/ui/progress";
import { Badge } from "@/components/ui/badge";
import { projectService } from '@/services/projectService';
import { taskService } from '@/services/taskService';
import { ProjectData } from '@/types/project';
import { TaskData } from '@/types/task';
import NoteList from '@/components/notes/NoteList';
import CreateTaskForm from './CreateTaskForm';
import { Loader2, Calendar, CheckCircle2, Clock, AlertCircle, Plus, X } from "lucide-react";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import { ScrollArea } from "@/components/ui/scroll-area";

interface ProjectDetailsProps {
  projectId: number;
}

const ProjectDetails: React.FC<ProjectDetailsProps> = ({ projectId }) => {
  const [project, setProject] = useState<ProjectData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showCreateTaskForm, setShowCreateTaskForm] = useState(false);

  const fetchProjectDetails = useCallback(async () => {
    try {
      const projectData = await projectService.getProjectById(projectId);
      setProject(projectData);
      setLoading(false);
    } catch (err) {
      console.error('Error fetching project details:', err);
      setError('Failed to fetch project details. Please try again.');
      setLoading(false);
    }
  }, [projectId]);

  useEffect(() => {
    fetchProjectDetails();
  }, [fetchProjectDetails]);

  const getProjectProgress = () => {
    if (!project || project.tasks.length === 0) return 0;
    const completedTasks = project.tasks.filter((task: { status: string; }) => task.status === 'Completed').length;
    return (completedTasks / project.tasks.length) * 100;
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'Completed':
        return 'bg-green-500 dark:bg-green-700';
      case 'In Progress':
        return 'bg-yellow-500 dark:bg-yellow-600';
      case 'Not Started':
        return 'bg-red-500 dark:bg-red-700';
      default:
        return 'bg-gray-500 dark:bg-gray-600';
    }
  };

  const handleTaskCreated = () => {
    setShowCreateTaskForm(false);
    fetchProjectDetails();
  };

  if (loading) return (
    <div className="flex justify-center items-center h-64">
      <Loader2 className="h-8 w-8 animate-spin text-primary" />
    </div>
  );

  if (error) return (
    <Alert variant="destructive">
      <AlertCircle className="h-4 w-4" />
      <AlertTitle>Error</AlertTitle>
      <AlertDescription>{error}</AlertDescription>
    </Alert>
  );

  if (!project) return (
    <Alert variant="default">
      <AlertCircle className="h-4 w-4" />
      <AlertTitle>Project Not Found</AlertTitle>
      <AlertDescription>The requested project could not be found.</AlertDescription>
    </Alert>
  );

  return (
    <div className="space-y-6 p-4 md:p-6">
      <Card>
        <CardHeader>
          <CardTitle className="text-xl md:text-2xl">{project.name}</CardTitle>
          <CardDescription className="flex flex-col sm:flex-row sm:items-center text-muted-foreground">
            <span className="flex items-center mb-2 sm:mb-0 sm:mr-4">
              <Calendar className="mr-2 h-4 w-4" />
              Start: {new Date(project.startDate).toLocaleDateString()}
            </span>
            <span className="flex items-center">
              <Calendar className="mr-2 h-4 w-4" />
              End: {new Date(project.endDate).toLocaleDateString()}
            </span>
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="mb-4">
            <div className="flex justify-between items-center mb-2">
              <span className="text-sm font-medium text-foreground">Project Progress</span>
              <span className="text-sm font-medium text-foreground">{getProjectProgress().toFixed(0)}% Complete</span>
            </div>
            <Progress value={getProjectProgress()} className="h-2" />
          </div>
          <p className="text-sm text-muted-foreground">{project.description}</p>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="flex flex-col sm:flex-row justify-between items-start sm:items-center">
            <span className="mb-2 sm:mb-0">Tasks</span>
            <Button size="sm" onClick={() => setShowCreateTaskForm(!showCreateTaskForm)}>
              {showCreateTaskForm ? <X className="h-4 w-4 mr-2" /> : <Plus className="h-4 w-4 mr-2" />}
              {showCreateTaskForm ? 'Cancel' : 'Add New Task'}
            </Button>
          </CardTitle>
        </CardHeader>
        <CardContent>
          {showCreateTaskForm && (
            <div className="mb-6">
              <CreateTaskForm projectId={projectId} onTaskCreated={handleTaskCreated} />
            </div>
          )}
          {project.tasks.length > 0 ? (
            <ScrollArea className="h-[300px] md:h-[400px]">
              <Accordion type="single" collapsible className="w-full">
                {project.tasks.map((task: TaskData) => (
                  <AccordionItem key={task.id} value={`task-${task.id}`}>
                    <AccordionTrigger className="hover:no-underline">
                      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center w-full">
                        <span className="font-medium mb-2 sm:mb-0">{task.name}</span>
                        <Badge className={`${getStatusColor(task.status)} text-primary-foreground`}>{task.status}</Badge>
                      </div>
                    </AccordionTrigger>
                    <AccordionContent>
                      <div className="space-y-2">
                        <p className="text-sm text-muted-foreground">{task.description}</p>
                        <div className="flex items-center text-sm text-muted-foreground">
                          <Clock className="mr-2 h-4 w-4" />
                          Due: {new Date(task.deadline).toLocaleDateString()}
                        </div>
                      </div>
                    </AccordionContent>
                  </AccordionItem>
                ))}
              </Accordion>
            </ScrollArea>
          ) : (
            <p className="text-muted-foreground">No tasks for this project yet.</p>
          )}
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Project Notes</CardTitle>
        </CardHeader>
        <CardContent>
          <NoteList parentId={projectId} parentType="project" />
        </CardContent>
      </Card>
    </div>
  );
};

export default ProjectDetails;