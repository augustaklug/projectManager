import React from 'react';
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"

interface Task {
  id: number;
  name: string;
  status: string;
  priority: string;
}

const TaskList = () => {
  // TODO: Fetch tasks from API
  const tasks: Task[] = [
    { id: 1, name: 'Task 1', status: 'In Progress', priority: 'High' },
    { id: 2, name: 'Task 2', status: 'Not Started', priority: 'Medium' },
    { id: 3, name: 'Task 3', status: 'Completed', priority: 'Low' },
  ];

  return (
    <div className="space-y-4">
      <h2 className="text-2xl font-bold">Tasks</h2>
      {tasks.map((task) => (
        <Card key={task.id}>
          <CardHeader>
            <CardTitle>{task.name}</CardTitle>
          </CardHeader>
          <CardContent>
            <p>Status: {task.status}</p>
            <Badge>{task.priority}</Badge>
            <Button className="mt-2">View Details</Button>
          </CardContent>
        </Card>
      ))}
    </div>
  );
};

export default TaskList;