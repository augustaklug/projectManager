import React, { useState, useEffect, useCallback } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { taskService } from '@/services/taskService';
import { projectService } from '@/services/projectService';
import { NoteData } from '@/types/note';
import NoteForm from './NoteForm';
import { Loader2, Plus, Edit, Trash2 } from "lucide-react";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";

interface NoteListProps {
  parentId: number;
  parentType: 'project' | 'task';
}

const NoteList: React.FC<NoteListProps> = ({ parentId, parentType }) => {
  const [notes, setNotes] = useState<NoteData[]>([]);
  const [isAddingNote, setIsAddingNote] = useState(false);
  const [editingNoteId, setEditingNoteId] = useState<number | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchNotes = useCallback(async () => {
    try {
      setLoading(true);
      const fetchedNotes = parentType === 'project'
        ? await projectService.getProjectNotes(parentId)
        : await taskService.getTaskNotes(parentId);
      setNotes(fetchedNotes);
    } catch (error) {
      console.error('Error fetching notes:', error);
      setError('Failed to fetch notes. Please try again.');
    } finally {
      setLoading(false);
    }
  }, [parentId, parentType]);

  useEffect(() => {
    fetchNotes();
  }, [fetchNotes]);

  const formatDate = (dateArray: number[] | undefined) => {
    if (!dateArray || !Array.isArray(dateArray)) return 'Unknown';

    const [year, month, day, hour, minute, second] = dateArray;
    const date = new Date(year, month - 1, day, hour, minute, second);

    return isNaN(date.getTime()) ? 'Invalid Date' : date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
  };

  const handleAddNote = async (newNote: NoteData) => {
    try {
      const createdNote = parentType === 'project'
        ? await projectService.addNoteToProject(parentId, newNote)
        : await taskService.addNoteToTask(parentId, newNote);
      setNotes([...notes, createdNote]);
      setIsAddingNote(false);
    } catch (error) {
      console.error('Error adding note:', error);
      setError('Failed to add note. Please try again.');
    }
  };

  const handleUpdateNote = async (updatedNote: NoteData) => {
    try {
      const updated = parentType === 'project'
        ? await projectService.updateProjectNote(parentId, updatedNote.id!, updatedNote)
        : await taskService.updateTaskNote(parentId, updatedNote.id!, updatedNote);
      setNotes(notes.map(note => note.id === updated.id ? updated : note));
      setEditingNoteId(null);
    } catch (error) {
      console.error('Error updating note:', error);
      setError('Failed to update note. Please try again.');
    }
  };

  const handleDeleteNote = async (id: number) => {
    try {
      parentType === 'project'
        ? await projectService.deleteProjectNote(id)
        : await taskService.deleteTaskNote(id);
      setNotes(notes.filter(note => note.id !== id));
    } catch (error) {
      console.error('Error deleting note:', error);
      setError('Failed to delete note. Please try again.');
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-24">
        <Loader2 className="h-6 w-6 animate-spin text-primary" />
      </div>
    );
  }

  if (error) {
    return (
      <Alert variant="destructive">
        <AlertTitle>Error</AlertTitle>
        <AlertDescription>{error}</AlertDescription>
      </Alert>
    );
  }

  return (
    <div className="space-y-4">
      <Button onClick={() => setIsAddingNote(true)} className="mb-4">
        <Plus className="mr-2 h-4 w-4" /> Add Note
      </Button>
      {isAddingNote && (
        <Card className="mb-4">
          <CardContent className="pt-6">
            <NoteForm
              onSubmit={handleAddNote}
              onCancel={() => setIsAddingNote(false)}
              parentId={parentId}
              parentType={parentType}
            />
          </CardContent>
        </Card>
      )}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {notes.map(note => (
          <Card
            key={note.id}
            className="flex flex-col bg-primary/5 dark:bg-primary/10 border-primary/10 dark:border-primary/20"
          >
            <CardHeader>
              <CardTitle className="text-lg font-medium">{note.content}</CardTitle>
              <p className="text-sm text-muted-foreground">Last Updated: {formatDate(note.lastUpdated)}</p>
            </CardHeader>
            <CardContent className="flex-grow">
              {editingNoteId === note.id ? (
                <NoteForm
                  note={note}
                  onSubmit={handleUpdateNote}
                  onCancel={() => setEditingNoteId(null)}
                  parentId={parentId}
                  parentType={parentType}
                />
              ) : (
                <div className="flex justify-end space-x-2 mt-4">
                  <Button size="sm" variant="outline" onClick={() => setEditingNoteId(note.id!)}>
                    <Edit className="h-4 w-4 mr-2" /> Edit
                  </Button>
                  <Button size="sm" variant="destructive" onClick={() => handleDeleteNote(note.id!)}>
                    <Trash2 className="h-4 w-4 mr-2" /> Delete
                  </Button>
                </div>
              )}
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
};

export default NoteList;