import React, { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { taskService } from '@/services/taskService';
import { TaskData, TaskStatus } from '@/types/task';
import NoteList from '@/components/notes/NoteList';

interface TaskDetailsProps {
  taskId: number;
}

const TaskDetails: React.FC<TaskDetailsProps> = ({ taskId }) => {
  const [task, setTask] = useState<TaskData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchTaskDetails = async () => {
    try {
      const taskData = await taskService.getTaskById(taskId);
      setTask(taskData);
      setLoading(false);
    } catch (err) {
      console.error('Error fetching task details:', err);
      setError('Failed to fetch task details. Please try again.');
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTaskDetails();
  }, [taskId]);

  const handleStatusChange = async (newStatus: TaskStatus) => {
    if (!task) return;
    try {
      await taskService.updateTask(task.id, { ...task, status: newStatus });
      setTask({ ...task, status: newStatus });
    } catch (err) {
      console.error('Error updating task status:', err);
    }
  };

  const getStatusColor = (status: TaskStatus) => {
    switch (status) {
      case 'Completed': return 'bg-green-500';
      case 'In Progress': return 'bg-yellow-500';
      case 'Not Started': return 'bg-red-500';
      default: return 'bg-gray-500';
    }
  };

  if (loading) return <div>Loading task details...</div>;
  if (error) return <div>Error: {error}</div>;
  if (!task) return <div>Task not found</div>;

  return (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <CardTitle>{task.name}</CardTitle>
        </CardHeader>
        <CardContent>
          <p>Deadline: {new Date(task.deadline).toLocaleDateString()}</p>
          <div className="mt-4">
            <p>Status:</p>
            <Select value={task.status} onValueChange={handleStatusChange}>
              <SelectTrigger className={`w-full ${getStatusColor(task.status)} text-white`}>
                <SelectValue placeholder="Select status" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="Not Started">Not Started</SelectItem>
                <SelectItem value="In Progress">In Progress</SelectItem>
                <SelectItem value="Completed">Completed</SelectItem>
                {task.status !== 'Not Started' &&
                 task.status !== 'In Progress' &&
                 task.status !== 'Completed' && (
                  <SelectItem value={task.status}>{task.status}</SelectItem>
                )}
              </SelectContent>
            </Select>
          </div>
          {task.assignedToId && (
            <p className="mt-4">Assigned to: {task.assignedToId}</p>
          )}
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Task Notes</CardTitle>
        </CardHeader>
        <CardContent>
          <NoteList parentId={taskId} parentType="task" />
        </CardContent>
      </Card>
    </div>
  );
};

export default TaskDetails;