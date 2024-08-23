import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Checkbox } from "@/components/ui/checkbox";
import { projectService } from '@/services/projectService';
import { userService, UserData } from '@/services/userService';

const CreateProject = () => {
  console.log('Rendering CreateProject component'); // Log para debug

  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [status, setStatus] = useState('');
  const [teamMembers, setTeamMembers] = useState<number[]>([]);
  const [allUsers, setAllUsers] = useState<UserData[]>([]);
  const [error, setError] = useState('');
  const router = useRouter();

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const users = await userService.getAllUsers();
        console.log('Fetched users:', users); // Log para debug
        setAllUsers(users);
      } catch (err) {
        console.error('Error fetching users:', err);
        setError('Failed to fetch users. Please try again.');
      }
    };
    fetchUsers();
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    try {
      const newProject = {
        name,
        description,
        startDate,
        endDate,
        status,
        teamMemberIds: teamMembers
      };

      await projectService.createProject(newProject);
      router.push('/projects');
    } catch (err) {
      console.error('Error creating project:', err);
      setError('Failed to create project. Please try again.');
    }
  };

  return (
    <Card className="w-[600px] mx-auto">
      <CardHeader>
        <CardTitle>Create New Project</CardTitle>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label htmlFor="name" className="block text-sm font-medium">Project Name</label>
            <Input
              id="name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>
          {/* Adicione os outros campos do formulário aqui */}
          <div>
            <label className="block text-sm font-medium">Team Members</label>
            <div className="mt-2 space-y-2">
              {allUsers.map((user) => (
                <div key={user.id} className="flex items-center">
                  <Checkbox
                    id={`user-${user.id}`}
                    checked={teamMembers.includes(user.id!)}
                    onCheckedChange={(checked) => {
                      if (checked && user.id) {
                        setTeamMembers([...teamMembers, user.id]);
                      } else {
                        setTeamMembers(teamMembers.filter(id => id !== user.id));
                      }
                    }}
                  />
                  <label htmlFor={`user-${user.id}`} className="ml-2">
                    {user.username}
                  </label>
                </div>
              ))}
            </div>
          </div>
          {error && <p className="text-red-500">{error}</p>}
          <Button type="submit">Create Project</Button>
        </form>
      </CardContent>
    </Card>
  );
};

export default CreateProject;