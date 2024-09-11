"use client"

import React, { useState, useEffect } from 'react'
import Link from 'next/link'
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Progress } from "@/components/ui/progress"
import { projectService } from '@/services/projectService'
import { taskService } from '@/services/taskService'
import { userService } from '@/services/userService'
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert'
import { AlertCircle, Loader2, BarChart2, CheckCircle2, Clock, Plus } from "lucide-react"
import { ProjectData, TaskData } from '@/types/project'
import { Button } from "@/components/ui/button"
import { ScrollArea } from "@/components/ui/scroll-area"

export default function Dashboard() {
  const [projects, setProjects] = useState<ProjectData[]>([])
  const [tasks, setTasks] = useState<TaskData[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [userName, setUserName] = useState('')

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true)
        const [projectsData, tasksData, userData] = await Promise.all([
          projectService.getUserProjects(),
          taskService.getUserTasks(),
          userService.getCurrentUser()
        ])
        setProjects(projectsData)
        setTasks(tasksData)
        setUserName(userData.username || 'User')
        setLoading(false)
      } catch (err) {
        console.error('Error fetching data:', err)
        setError('Failed to fetch data. Please try again later.')
        setLoading(false)
      }
    }

    fetchData()
  }, [])

  const calculateProjectProgress = (project: ProjectData) => {
    const totalTasks = project.tasks.length
    if (totalTasks === 0) return 0
    const completedTasks = project.tasks.filter(task => task.status === 'Completed').length
    return Math.round((completedTasks / totalTasks) * 100)
  }

  if (loading) return (
    <div className="flex items-center justify-center h-screen">
      <Loader2 className="mr-2 h-16 w-16 animate-spin text-primary" />
    </div>
  )

  if (error) return (
    <Alert variant="destructive" className="m-4">
      <AlertCircle className="h-4 w-4" />
      <AlertTitle>Error</AlertTitle>
      <AlertDescription>{error}</AlertDescription>
    </Alert>
  )

  const tasksInProgress = tasks.filter(task => task.status === 'In Progress').length
  const completedTasks = tasks.filter(task => task.status === 'Completed').length

  const NoDataAlert = ({ title, description }: { title: string; description: string }) => (
    <Alert variant="default" className="mt-4">
      <AlertCircle className="h-4 w-4" />
      <AlertTitle>{title}</AlertTitle>
      <AlertDescription>{description}</AlertDescription>
    </Alert>
  )

  return (
    <div className="min-h-screen bg-background text-foreground">
      <header className="sticky top-0 z-10 bg-background border-b border-border shadow-sm">
        <div className="container mx-auto px-4 py-4 flex justify-between items-center">
          <h1 className="text-2xl font-bold">Dashboard</h1>
          <Button asChild size="sm">
            <Link href="/projects/create">
              <Plus className="mr-2 h-4 w-4" /> New Project
            </Link>
          </Button>
        </div>
      </header>
      <main className="container mx-auto px-4 py-8">
        <div className="mb-8">
          <h2 className="text-3xl font-bold mb-2">Welcome back, {userName}!</h2>
          <p className="text-muted-foreground">Here&apos;s an overview of your projects and tasks.</p>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
          <StatCard title="Total Projects" value={projects.length} icon={<BarChart2 />} />
          <StatCard title="Tasks in Progress" value={tasksInProgress} icon={<Clock />} />
          <StatCard title="Completed Tasks" value={completedTasks} icon={<CheckCircle2 />} />
        </div>

        <Card className="bg-card text-card-foreground mb-8">
          <CardHeader>
            <CardTitle>Project Progress</CardTitle>
          </CardHeader>
          <CardContent>
            {projects.length > 0 ? (
              <ScrollArea className="h-[300px] pr-4">
                <div className="space-y-4">
                  {projects.map((project) => (
                    <ProjectProgressBar key={project.id} project={project} />
                  ))}
                </div>
              </ScrollArea>
            ) : (
              <NoDataAlert
                title="No Projects"
                description="You don't have any projects yet. Start by creating a new project!"
              />
            )}
          </CardContent>
        </Card>

        {tasks.length === 0 && (
          <NoDataAlert
            title="No Tasks"
            description="You don't have any tasks yet. Start by creating a new task in one of your projects!"
          />
        )}
      </main>
    </div>
  )
}

const StatCard = ({ title, value, icon }: { title: string; value: number; icon: React.ReactNode }) => (
  <Card className="bg-card text-card-foreground">
    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
      <CardTitle className="text-sm font-medium">{title}</CardTitle>
      {React.cloneElement(icon as React.ReactElement, { className: "h-4 w-4 text-muted-foreground" })}
    </CardHeader>
    <CardContent>
      <div className="text-2xl font-bold">{value}</div>
      <p className="text-xs text-muted-foreground">
        {value === 1 ? title.replace(/s$/, '') : title}
      </p>
    </CardContent>
  </Card>
)

const ProjectProgressBar = ({ project }: { project: ProjectData }) => {
  const progress = calculateProjectProgress(project)
  return (
    <div className="space-y-2">
      <div className="flex justify-between items-center">
        <Link href={`/projects/${project.id}`} className="text-primary hover:underline font-medium">
          {project.name}
        </Link>
        <span className="text-sm font-medium">{progress}% Complete</span>
      </div>
      <Progress value={progress} className="h-2" />
    </div>
  )
}

function calculateProjectProgress(project: ProjectData) {
  const totalTasks = project.tasks.length
  if (totalTasks === 0) return 0
  const completedTasks = project.tasks.filter(task => task.status === 'Completed').length
  return Math.round((completedTasks / totalTasks) * 100)
}