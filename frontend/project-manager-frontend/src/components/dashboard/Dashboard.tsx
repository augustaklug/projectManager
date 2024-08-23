import React, { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Progress } from "@/components/ui/progress"
import { projectService } from '@/services/projectService';
import { taskService } from '@/services/taskService';
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert';
import { AlertCircle } from "lucide-react";

interface Project {
  id: number;
  name: string;
  tasksCompleted: number;
  totalTasks: number;
}

interface Task {
  id: number;
  name: string;
  status: string;
}

const Dashboard = () => {
  const [projects, setProjects] = useState<Project[]>([]);
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [projectsData, tasksData] = await Promise.all([
          projectService.getUserProjects(),
          taskService.getUserTasks()
        ]);
        setProjects(projectsData);
        setTasks(tasksData);
        setLoading(false);
      } catch (err) {
        console.error('Error fetching data:', err);
        setError('Failed to fetch data. Please try again later.');
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;

  const tasksInProgress = tasks.filter(task => task.status === 'In Progress').length;
  const completedTasks = tasks.filter(task => task.status === 'Completed').length;

  const NoDataAlert = ({ title, description }: { title: string; description: string }) => (
    <Alert variant="default">
      <AlertCircle className="h-4 w-4" />
      <AlertTitle>{title}</AlertTitle>
      <AlertDescription>{description}</AlertDescription>
    </Alert>
  );

  return (
    <div className="space-y-6">
      <h1 className="text-3xl font-bold">Dashboard</h1>

      <Card>
        <CardHeader>
          <CardTitle>Project Progress</CardTitle>
        </CardHeader>
        <CardContent>
          {projects.length > 0 ? (
            <div className="space-y-4">
              {projects.map((project) => (
                <div key={project.id} className="space-y-2">
                  <div className="flex justify-between">
                    <span>{project.name}</span>
                    <span>{project.tasksCompleted}/{project.totalTasks} tasks</span>
                  </div>
                  <Progress value={(project.tasksCompleted / project.totalTasks) * 100} />
                </div>
              ))}
            </div>
          ) : (
            <NoDataAlert
              title="No Projects"
              description="You don't have any projects yet. Start by creating a new project!"
            />
          )}
        </CardContent>
      </Card>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <Card>
          <CardHeader>
            <CardTitle>Total Projects</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-4xl font-bold">{projects.length}</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Tasks in Progress</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-4xl font-bold">{tasksInProgress}</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Completed Tasks</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-4xl font-bold">{completedTasks}</p>
          </CardContent>
        </Card>
      </div>

      {tasks.length === 0 && (
        <NoDataAlert
          title="No Tasks"
          description="You don't have any tasks yet. Start by creating a new task in one of your projects!"
        />
      )}
    </div>
  );
};

export default Dashboard;