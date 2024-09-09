import React, {useState, useEffect, useCallback} from 'react';
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import {taskService} from '@/services/taskService';
import {projectService} from '@/services/projectService';
import {ProjectData, TaskData} from '@/types/project';

interface TaskWithProject extends TaskData {
    projectName?: string;
}

const TaskList: React.FC = () => {
    const [tasks, setTasks] = useState<TaskWithProject[]>([]);
    const [filteredTasks, setFilteredTasks] = useState<TaskWithProject[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [statusFilter, setStatusFilter] = useState('all');

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

            setTasks(tasksWithProjects);
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

    const filterTasks = useCallback(() => {
        if (statusFilter === 'all') {
            setFilteredTasks(tasks);
        } else {
            setFilteredTasks(tasks.filter(task => task.status.toLowerCase() === statusFilter));
        }
    }, [tasks, statusFilter]);

    useEffect(() => {
        filterTasks();
    }, [filterTasks]);

    const getStatusColor = (status: string) => {
        switch (status.toLowerCase()) {
            case 'completed':
                return 'bg-green-500';
            case 'in progress':
                return 'bg-yellow-500';
            case 'not started':
                return 'bg-red-500';
            default:
                return 'bg-gray-500';
        }
    };

    const handleStatusChange = async (taskId: number | undefined, newStatus: string) => {
        if (taskId === undefined) {
            console.error('Task ID is undefined');
            return;
        }
        try {
            await taskService.updateTask(taskId, {status: newStatus});
            setTasks(tasks.map(task =>
                task.id === taskId ? {...task, status: newStatus} : task
            ));
        } catch (err) {
            console.error('Error updating task status:', err);
        }
    };

    if (loading) return <div>Loading tasks...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div className="space-y-4">
            <div className="flex justify-between items-center">
                <h2 className="text-2xl font-bold">My Tasks</h2>
                <Select value={statusFilter} onValueChange={setStatusFilter}>
                    <SelectTrigger className="w-[180px]">
                        <SelectValue placeholder="Filter by status"/>
                    </SelectTrigger>
                    <SelectContent>
                        <SelectItem value="all">All</SelectItem>
                        <SelectItem value="not started">Not Started</SelectItem>
                        <SelectItem value="in progress">In Progress</SelectItem>
                        <SelectItem value="completed">Completed</SelectItem>
                    </SelectContent>
                </Select>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
                {filteredTasks.length > 0 ? (
                    filteredTasks.map((task) => (
                        <Card key={task.id} className="flex flex-col">
                            <CardHeader className="flex-grow">
                                <CardTitle className="text-lg">{task.name}</CardTitle>
                                <p className="text-sm text-gray-500">{task.projectName}</p>
                            </CardHeader>
                            <CardContent>
                                <p className="text-sm mb-2">
                                    Deadline: {task.deadline ? new Date(task.deadline).toLocaleDateString() : 'Not set'}
                                </p>
                                <Select
                                    value={task.status}
                                    onValueChange={(value) => handleStatusChange(task.id, value)}
                                >
                                    <SelectTrigger className={`w-full ${getStatusColor(task.status)} text-white`}>
                                        <SelectValue placeholder="Select status"/>
                                    </SelectTrigger>
                                    <SelectContent>
                                        <SelectItem value="Not Started">Not Started</SelectItem>
                                        <SelectItem value="In Progress">In Progress</SelectItem>
                                        <SelectItem value="Completed">Completed</SelectItem>
                                    </SelectContent>
                                </Select>
                            </CardContent>
                        </Card>
                    ))
                ) : (
                    <p className="col-span-full text-center">No tasks found.</p>
                )}
            </div>
        </div>
    );
};

export default TaskList;