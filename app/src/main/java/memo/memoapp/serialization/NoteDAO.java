package memo.memoapp.serialization;

import memo.memoapp.model.Note;
import memo.memoapp.model.ObservableList;

/**
 * Created by Angelo Faella
 */

public interface NoteDAO {

    boolean save(Note note);

    boolean remove(Note note);

    Note retrieve(String title);

    ObservableList<Note> retrieveAllNotes();
}
