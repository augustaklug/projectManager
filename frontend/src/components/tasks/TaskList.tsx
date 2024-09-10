import React, { useState, useEffect, useCallback } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { taskService } from '@/services/taskService';
import { projectService } from '@/services/projectService';
import { ProjectData, TaskData } from '@/types/project';
import { Loader2 } from "lucide-react";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";

interface TaskWithProject extends TaskData {
  projectName?: string;
}

const TaskList: React.FC = () => {
  const [tasks, setTasks] = useState<TaskWithProject[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchTasksAndProjects = useCallback(async () => {
    try {
      setLoading(true);
      const tasksData = await taskService.getUserTasks();
      const projectIds = Array.from(new Set(tasksData.map((task: TaskData) => task.projectId))).filter((id): id is number => id !== undefined);
      const projects = await Promise.all(projectIds.map((id) => projectService.getProjectById(id)));

      const projectMap = new Map(projects.map((project: ProjectData) => [project.id, project.name]));

      const tasksWithProjects = tasksData.map((task: TaskData) => ({
        ...task,
        projectName: task.projectId !== undefined ? projectMap.get(task.projectId) || 'Unknown Project' : 'Unknown Project'
      }));

      const sortedTasks = tasksWithProjects.sort((a: { deadline: string | number | Date; }, b: { deadline: string | number | Date; }) => {
        if (!a.deadline) return 1;
        if (!b.deadline) return -1;
        return new Date(a.deadline).getTime() - new Date(b.deadline).getTime();
      });

      setTasks(sortedTasks);
      setLoading(false);
    } catch (err) {
      console.error('Error fetching tasks and projects:', err);
      setError('Failed to fetch tasks. Please try again.');
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchTasksAndProjects();
  }, [fetchTasksAndProjects]);

  const getStatusColor = (status: string) => {
    switch (status.toLowerCase()) {
      case 'completed':
        return 'bg-green-500 dark:bg-green-700';
      case 'in progress':
        return 'bg-yellow-500 dark:bg-yellow-600';
      case 'not started':
        return 'bg-red-500 dark:bg-red-700';
      default:
        return 'bg-gray-500 dark:bg-gray-600';
    }
  };

  const isDeadlineNear = (deadline: string | undefined, status: string) => {
    if (status.toLowerCase() === 'completed' || !deadline) return false;
    const deadlineDate = new Date(deadline);
    const today = new Date();
    const diffTime = deadlineDate.getTime() - today.getTime();
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays <= 3 && diffDays >= 0;
  };

  const handleStatusChange = async (taskId: number | undefined, newStatus: string) => {
    if (taskId === undefined) {
      console.error('Task ID is undefined');
      return;
    }
    try {
      await taskService.updateTask(taskId, { status: newStatus });
      setTasks(prevTasks => {
        const updatedTasks = prevTasks.map(task =>
          task.id === taskId ? { ...task, status: newStatus } : task
        );
        return updatedTasks.sort((a, b) => {
          if (!a.deadline) return 1;
          if (!b.deadline) return -1;
          return new Date(a.deadline).getTime() - new Date(b.deadline).getTime();
        });
      });
    } catch (err) {
      console.error('Error updating task status:', err);
    }
  };

  if (loading) return (
    <div className="flex justify-center items-center h-64">
      <Loader2 className="h-8 w-8 animate-spin text-primary" />
    </div>
  );

  if (error) return (
    <Alert variant="destructive">
      <AlertTitle>Error</AlertTitle>
      <AlertDescription>{error}</AlertDescription>
    </Alert>
  );

  const statusColumns = ['Not Started', 'In Progress', 'Completed'];

  return (
    <div className="space-y-4">
      <h2 className="text-2xl font-bold text-foreground">My Tasks</h2>
      <div className="flex space-x-4 overflow-x-auto pb-4">
        {statusColumns.map((status) => (
          <div key={status} className="flex-1 min-w-[300px]">
            <h3 className="text-lg font-semibold mb-2 text-foreground p-2 rounded">{status}</h3>
            <div className="space-y-4">
              {tasks.filter(task => task.status === status).map((task) => (
                <Card
                  key={task.id}
                  className={`flex flex-col ${isDeadlineNear(task.deadline, task.status) ? 'bg-orange-100 dark:bg-orange-900/30 border-orange-300 dark:border-orange-700' : ''}`}
                >
                  <CardHeader className="flex-grow">
                    <CardTitle className="text-lg">{task.name}</CardTitle>
                    <p className="text-sm text-muted-foreground">{task.projectName}</p>
                  </CardHeader>
                  <CardContent>
                    <p className={`text-sm mb-2 ${isDeadlineNear(task.deadline, task.status) ? 'font-bold text-orange-600 dark:text-orange-400' : ''}`}>
                      Deadline: {task.deadline ? new Date(task.deadline).toLocaleDateString() : 'Not set'}
                    </p>
                    <Select
                      value={task.status}
                      onValueChange={(value) => handleStatusChange(task.id, value)}
                    >
                      <SelectTrigger className={`w-full ${getStatusColor(task.status)} text-primary-foreground`}>
                        <SelectValue placeholder="Select status" />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="Not Started">Not Started</SelectItem>
                        <SelectItem value="In Progress">In Progress</SelectItem>
                        <SelectItem value="Completed">Completed</SelectItem>
                      </SelectContent>
                    </Select>
                  </CardContent>
                </Card>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default TaskList;