import React, { useState, useEffect } from 'react';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { NoteData } from '@/types/note';

interface NoteFormProps {
  note?: NoteData;
  onSubmit: (note: NoteData) => void;
  onCancel: () => void;
  parentId: number;
  parentType: 'project' | 'task';
}

const NoteForm: React.FC<NoteFormProps> = ({ note, onSubmit, onCancel, parentId, parentType }) => {
  const [content, setContent] = useState(note?.content || '');

  useEffect(() => {
    if (note) {
      setContent(note.content);
    }
  }, [note]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const updatedNote: NoteData = {
      ...note,
      content,
      [parentType === 'project' ? 'projectId' : 'taskId']: parentId,
    };
    onSubmit(updatedNote);
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <Textarea
        value={content}
        onChange={(e) => setContent(e.target.value)}
        placeholder="Note content"
        required
      />
      <div>
        <Button type="submit">{note ? 'Update' : 'Add'} Note</Button>
        <Button type="button" onClick={onCancel} variant="outline">Cancel</Button>
      </div>
    </form>
  );
};

export default NoteForm;