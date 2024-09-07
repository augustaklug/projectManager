import React, { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Progress } from "@/components/ui/progress";
import { Badge } from "@/components/ui/badge";
import { projectService } from '@/services/projectService';
import { taskService } from '@/services/taskService';
import { ProjectData } from '@/types/project';
import { TaskData } from '@/types/task';
import NoteList from '@/components/notes/NoteList';
import CreateTaskForm from './CreateTaskForm';

interface ProjectDetailsProps {
  projectId: number;
}

const ProjectDetails: React.FC<ProjectDetailsProps> = ({ projectId }) => {
  const [project, setProject] = useState<ProjectData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showCreateTaskForm, setShowCreateTaskForm] = useState(false);

  const fetchProjectDetails = async () => {
    try {
      const projectData = await projectService.getProjectById(projectId);
      setProject(projectData);
      setLoading(false);
    } catch (err) {
      console.error('Error fetching project details:', err);
      setError('Failed to fetch project details. Please try again.');
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProjectDetails();
  }, [projectId]);

  const getProjectProgress = () => {
    if (!project || project.tasks.length === 0) return 0;
    const completedTasks = project.tasks.filter((task: { status: string; }) => task.status === 'Completed').length;
    return (completedTasks / project.tasks.length) * 100;
  };

  const getStatusColor = (status: string | number | bigint | boolean | React.ReactElement<any, string | React.JSXElementConstructor<any>> | Iterable<React.ReactNode> | Promise<React.AwaitedReactNode> | null | undefined) => {
    switch (status) {
      case 'Completed': return 'bg-green-500';
      case 'In Progress': return 'bg-yellow-500';
      case 'Not Started': return 'bg-red-500';
      default: return 'bg-gray-500';
    }
  };

  const handleTaskCreated = () => {
    setShowCreateTaskForm(false);
    fetchProjectDetails();
  };

  if (loading) return <div>Loading project details...</div>;
  if (error) return <div>Error: {error}</div>;
  if (!project) return <div>Project not found</div>;

  return (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <CardTitle>{project.name}</CardTitle>
        </CardHeader>
        <CardContent>
          <p>Start Date: {new Date(project.startDate).toLocaleDateString()}</p>
          <p>End Date: {new Date(project.endDate).toLocaleDateString()}</p>
          <div className="mt-4">
            <p>Project Progress:</p>
            <Progress value={getProjectProgress()} className="mt-2" />
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Tasks</CardTitle>
        </CardHeader>
        <CardContent>
          {project.tasks.length > 0 ? (
            <ul className="space-y-4">
              {project.tasks.map((task: { id: React.Key | null | undefined; name: string | number | bigint | boolean | React.ReactElement<any, string | React.JSXElementConstructor<any>> | Iterable<React.ReactNode> | React.ReactPortal | Promise<React.AwaitedReactNode> | null | undefined; status: string | number | bigint | boolean | React.ReactElement<any, string | React.JSXElementConstructor<any>> | Iterable<React.ReactNode> | Promise<React.AwaitedReactNode> | null | undefined; deadline: string | number | Date; }) => (
                <li key={task.id} className="flex justify-between items-center">
                  <span>{task.name}</span>
                  <div>
                    <Badge className={`mr-2 ${getStatusColor(task.status)}`}>{task.status}</Badge>
                    <span className="text-sm text-gray-500">Due: {new Date(task.deadline).toLocaleDateString()}</span>
                  </div>
                </li>
              ))}
            </ul>
          ) : (
            <p>No tasks for this project yet.</p>
          )}
          <Button className="mt-4" onClick={() => setShowCreateTaskForm(!showCreateTaskForm)}>
            {showCreateTaskForm ? 'Cancel' : 'Add New Task'}
          </Button>
          {showCreateTaskForm && (
            <div className="mt-4">
              <CreateTaskForm projectId={projectId} onTaskCreated={handleTaskCreated} />
            </div>
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