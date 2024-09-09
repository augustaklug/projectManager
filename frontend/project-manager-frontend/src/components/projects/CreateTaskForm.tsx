import React, { useState, useEffect } from 'react';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { taskService } from '@/services/taskService';
import { userService, UserData } from '@/services/userService';
import { projectService } from '@/services/projectService';
import { TaskData } from '@/types/task';
import { ProjectData } from '@/types/project';

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
        console.log("Fetching project details for projectId:", projectId);
        const project: ProjectData = await projectService.getProjectById(projectId);
        console.log("Received project data:", project);

        if (project.teamMemberIds && project.teamMemberIds.length > 0) {
          console.log("Team member IDs:", project.teamMemberIds);
          const members = await Promise.all(
            project.teamMemberIds.map(id => userService.getUserById(id))
          );
          console.log("Fetched team members:", members);
          setProjectMembers(members);
        } else {
          console.log("No team members found for this project");
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

    console.log("Submitting task data:", taskData);

    try {
      const createdTask = await taskService.createTask(taskData);
      console.log("Task created successfully:", createdTask);
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

  console.log("Current state - projectMembers:", projectMembers);
  console.log("Current state - loading:", loading);
  console.log("Current state - error:", error);

  if (loading) {
    return <div>Loading project members...</div>;
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <Input
        placeholder="Task Name"
        value={name}
        onChange={(e) => setName(e.target.value)}
        required
      />
      <Select value={status} onValueChange={setStatus}>
        <SelectTrigger>
          <SelectValue placeholder="Status" />
        </SelectTrigger>
        <SelectContent>
          <SelectItem value="Not Started">Not Started</SelectItem>
          <SelectItem value="In Progress">In Progress</SelectItem>
        </SelectContent>
      </Select>
      <Input
        type="date"
        value={deadline}
        onChange={(e) => setDeadline(e.target.value)}
      />
      <Select value={assignedToId} onValueChange={setAssignedToId}>
        <SelectTrigger>
          <SelectValue placeholder="Assign to" />
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
      {error && <p className="text-red-500">{error}</p>}
      <Button type="submit">Create Task</Button>
    </form>
  );
};

export default CreateTaskForm;