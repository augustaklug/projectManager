import { TaskData } from './task';
import { NoteData } from './note';

export type { TaskData };

export interface ProjectData {
  id: number;
  name: string;
  description?: string;
  startDate: string; // ISO date string
  endDate: string; // ISO date string
  status?: 'Not Started' | 'In Progress' | 'Completed'; // Tornar status opcional
  teamMemberIds: number[];
  tasks: TaskData[];
  createdAt?: string; // ISO date string
  updatedAt?: string; // ISO date string
}

export interface ProjectWithNotes extends ProjectData {
  notes: NoteData[];
}