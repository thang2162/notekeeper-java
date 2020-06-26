package daos;

import java.util.List;

import models.Note;

public interface NoteDao {
	public List<Note> getAllNotes(String email);
	public Boolean newNote (Note note);
	public Boolean updateNote(Note note);
	public Boolean deleteNote (Integer id);
	public Boolean deleteAllNotes (String email);
}
