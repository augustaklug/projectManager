import { NoteData } from './note';

export type TaskStatus = 'Not Started' | 'In Progress' | 'Completed' | string;

export interface TaskData {
  id?: number;
  name: string;
  description?: string;
  status: TaskStatus;
  deadline: string; // ISO date string
  projectId: number;
  assignedToId?: number;
  createdAt?: string; // ISO date string
  updatedAt?: string; // ISO date string
}

export interface TaskWithNotes extends TaskData {
  notes: NoteData[];
}