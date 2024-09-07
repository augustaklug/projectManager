import React, {useState, useEffect} from 'react';
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import {Button} from "@/components/ui/button";
import {taskService} from '@/services/taskService';
import {projectService} from '@/services/projectService';
import {NoteData} from '@/types/note';
import NoteForm from './NoteForm';

interface NoteListProps {
    parentId: number;
    parentType: 'project' | 'task';
}

const NoteList: React.FC<NoteListProps> = ({parentId, parentType}) => {
    const [notes, setNotes] = useState<NoteData[]>([]);
    const [isAddingNote, setIsAddingNote] = useState(false);
    const [editingNoteId, setEditingNoteId] = useState<number | null>(null);

    useEffect(() => {
        fetchNotes();
    }, [parentId, parentType]);

    const fetchNotes = async () => {
        try {
            const fetchedNotes = parentType === 'project'
                ? await projectService.getProjectNotes(parentId)
                : await taskService.getTaskNotes(parentId);

            console.log("Fetched notes:", fetchedNotes);  // Verificar os dados recebidos
            setNotes(fetchedNotes);
        } catch (error) {
            console.error('Error fetching notes:', error);
        }
    };

    const formatDate = (dateArray: number[] | undefined) => {
        if (!dateArray || !Array.isArray(dateArray)) return 'Unknown';

        // Criar uma nova data a partir do array de números
        const [year, month, day, hour, minute, second] = dateArray;
        const date = new Date(year, month - 1, day, hour, minute, second); // O mês é baseado em zero no JS

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
        }
    };

    return (
        <div className="space-y-4">
            <Button onClick={() => setIsAddingNote(true)}>Add Note</Button>
            {isAddingNote && (
                <NoteForm
                    onSubmit={handleAddNote}
                    onCancel={() => setIsAddingNote(false)}
                    parentId={parentId}
                    parentType={parentType}
                />
            )}
            {notes.map(note => (
                <Card key={note.id}>
                    <CardHeader>
                        <CardTitle>Note {note.id}</CardTitle>
                    </CardHeader>
                    <CardContent>
                        <p>{note.content}</p>
                        <p className="text-sm text-gray-500">Last Updated: {formatDate(note.lastUpdated)}</p>
                        {editingNoteId === note.id ? (
                            <NoteForm
                                note={note}
                                onSubmit={handleUpdateNote}
                                onCancel={() => setEditingNoteId(null)}
                                parentId={parentId}
                                parentType={parentType}
                            />
                        ) : (
                            <div className="mt-2">
                                <Button onClick={() => setEditingNoteId(note.id!)}>Edit</Button>
                                <Button variant="destructive" onClick={() => handleDeleteNote(note.id!)}>Delete</Button>
                            </div>
                        )}
                    </CardContent>
                </Card>
            ))}
        </div>
    );
};

export default NoteList;
