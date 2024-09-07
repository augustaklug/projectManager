import React, { useState, useEffect } from 'react';
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";
import { NoteData } from '@/types/note';

interface NoteFormProps {
  note?: NoteData;
  onSubmit: (note: NoteData) => void;
  onCancel: () => void;
  parentId: number;
  parentType: 'project' | 'task';
}

const MAX_CHARACTERS = 255;

const NoteForm: React.FC<NoteFormProps> = ({ note, onSubmit, onCancel, parentId, parentType }) => {
  const [content, setContent] = useState(note?.content || '');
  const [error, setError] = useState('');

  useEffect(() => {
    if (note) {
      setContent(note.content);
    }
  }, [note]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (content.length > MAX_CHARACTERS) {
      setError(`Note content must not exceed ${MAX_CHARACTERS} characters.`);
      return;
    }
    const updatedNote: NoteData = {
      ...note,
      content,
      [parentType === 'project' ? 'projectId' : 'taskId']: parentId,
    };
    onSubmit(updatedNote);
  };

  const handleContentChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    const newContent = e.target.value;
    setContent(newContent);
    if (newContent.length > MAX_CHARACTERS) {
      setError(`Note content must not exceed ${MAX_CHARACTERS} characters.`);
    } else {
      setError('');
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div>
        <Textarea
          value={content}
          onChange={handleContentChange}
          placeholder="Note content"
          className={`resize-none ${error ? 'border-red-500' : ''}`}
          rows={4}
        />
        <div className="flex justify-between items-center mt-2">
          <span className={`text-sm ${content.length > MAX_CHARACTERS ? 'text-red-500' : 'text-gray-500'}`}>
            {content.length}/{MAX_CHARACTERS}
          </span>
          {error && <span className="text-sm text-red-500">{error}</span>}
        </div>
      </div>
      <div className="flex justify-end space-x-2">
        <Button type="submit" disabled={content.length === 0 || content.length > MAX_CHARACTERS}>
          {note ? 'Update' : 'Add'} Note
        </Button>
        <Button type="button" onClick={onCancel} variant="outline">
          Cancel
        </Button>
      </div>
    </form>
  );
};

export default NoteForm;