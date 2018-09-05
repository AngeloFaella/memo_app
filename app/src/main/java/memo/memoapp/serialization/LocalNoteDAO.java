package memo.memoapp.serialization;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;

import memo.memoapp.R;
import memo.memoapp.controller.Service;
import memo.memoapp.model.Note;
import memo.memoapp.model.ObservableList;

/**
 * Created by Angelo Faella
 */

public class LocalNoteDAO implements NoteDAO {

    private static final String MEMO_NOTES = "MEMO_NOTES";
    private static LocalNoteDAO instance=null;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ObservableList<Note> notes;

    private LocalNoteDAO(Context context){
        preferences = context.getSharedPreferences(
                context.getString(R.string.notes_file), Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static LocalNoteDAO getInstance(Context context){
        if(instance == null) instance = new LocalNoteDAO(context);
        return instance;
    }

    @Override
    public boolean save(Note note) {
        if(notes.contains(note)) return false;
        notes.add(0,note);
        Log.e(Service.LOG_TAG,"Added: "+note.getTitle());
        return save(notes);
    }

    private boolean save(List<Note> notes) {
        String json = NoteJson.notesToJson(notes);
        editor.putString(MEMO_NOTES,json);
        editor.apply();
        Log.e(Service.LOG_TAG,"Saved "+notes.size()+" notes");
        return true;
    }

    @Override
    public boolean remove(Note note) {
        if(! notes.remove(note)) return false;
        Log.e(Service.LOG_TAG,"Removed: "+note.getTitle());
        return save(notes);
    }

    @Override
    public Note retrieve(String title) {
        retrieveAllNotes();
        for (Note n : notes){
            if(n.getTitle().equals(title)) return n;
        }
        return null;
    }

    @Override
    public ObservableList<Note> retrieveAllNotes() {
        if(notes != null) return notes;

        String notesJson = preferences.getString(MEMO_NOTES,null);
        notes = NoteJson.NotesFromJson(notesJson);
        if(notes == null) notes = new ObservableList<Note>();

        return notes;
    }
}
