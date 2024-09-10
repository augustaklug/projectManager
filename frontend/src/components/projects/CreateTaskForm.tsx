import React, { useState, useEffect } from 'react';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { taskService } from '@/services/taskService';
import { userService, UserData } from '@/services/userService';
import { projectService } from '@/services/projectService';
import { TaskData } from '@/types/task';
import { ProjectData } from '@/types/project';
import { Loader2, Calendar, User } from "lucide-react";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";

interface CreateTaskFormProps {
  projectId: number;
  onTaskCreated: () => void;
}

const CreateTaskForm: React.FC<CreateTaskFormProps> = ({ projectId, onTaskCreated }) => {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [status, setStatus] = useState('Not Started');
  const [deadline, setDeadline] = useState('');
  const [assignedToId, setAssignedToId] = useState('unassigned');
  const [error, setError] = useState('');
  const [projectMembers, setProjectMembers] = useState<UserData[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProjectDetails = async () => {
      try {
        setLoading(true);
        const project: ProjectData = await projectService.getProjectById(projectId);

        if (project.teamMemberIds && project.teamMemberIds.length > 0) {
          const members = await Promise.all(
            project.teamMemberIds.map(id => userService.getUserById(id))
          );
          setProjectMembers(members);
        }
      } catch (err) {
        console.error('Error fetching project details:', err);
        setError('Failed to fetch project members');
      } finally {
        setLoading(false);
      }
    };

    fetchProjectDetails();
  }, [projectId]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    const taskData: TaskData = {
      name,
      description,
      status,
      deadline,
      projectId,
      assignedToId: assignedToId !== 'unassigned' ? parseInt(assignedToId) : undefined
    };

    try {
      await taskService.createTask(taskData);
      setName('');
      setDescription('');
      setStatus('Not Started');
      setDeadline('');
      setAssignedToId('unassigned');
      onTaskCreated();
    } catch (err) {
      console.error('Error creating task:', err);
      setError('Failed to create task. Please try again.');
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-24">
        <Loader2 className="h-6 w-6 animate-spin text-primary" />
      </div>
    );
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div className="space-y-2">
        <Label htmlFor="taskName">Task Name</Label>
        <Input
          id="taskName"
          placeholder="Enter task name"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
        />
      </div>
      <div className="space-y-2">
        <Label htmlFor="taskStatus">Status</Label>
        <Select value={status} onValueChange={setStatus}>
          <SelectTrigger id="taskStatus">
            <SelectValue placeholder="Select status" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="Not Started">Not Started</SelectItem>
            <SelectItem value="In Progress">In Progress</SelectItem>
          </SelectContent>
        </Select>
      </div>
      <div className="space-y-2">
        <Label htmlFor="taskDeadline" className="flex items-center">
          <Calendar className="mr-2 h-4 w-4" />
          Deadline
        </Label>
        <Input
          id="taskDeadline"
          type="date"
          value={deadline}
          onChange={(e) => setDeadline(e.target.value)}
        />
      </div>
      <div className="space-y-2">
        <Label htmlFor="taskAssignee" className="flex items-center">
          <User className="mr-2 h-4 w-4" />
          Assign to
        </Label>
        <Select value={assignedToId} onValueChange={setAssignedToId}>
          <SelectTrigger id="taskAssignee">
            <SelectValue placeholder="Select team member" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="unassigned">Unassigned</SelectItem>
            {projectMembers.map((member) => (
              <SelectItem key={member.id} value={member.id?.toString() ?? ''}>
                {member.username}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>
      {error && (
        <Alert variant="destructive">
          <AlertTitle>Error</AlertTitle>
          <AlertDescription>{error}</AlertDescription>
        </Alert>
      )}
      <Button type="submit" className="w-full">Create Task</Button>
    </form>
  );
};

export default CreateTaskForm;