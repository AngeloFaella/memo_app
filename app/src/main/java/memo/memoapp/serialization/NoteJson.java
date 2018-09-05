package memo.memoapp.serialization;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import memo.memoapp.model.Note;
import memo.memoapp.model.ObservableList;

/**
 * Created by Angelo Faella
 */

public class NoteJson {

    public static String noteToJson(Note note){
        Gson gson = new Gson();
        return gson.toJson(note);
    }

    public static String notesToJson(List<Note> notes){
        Gson gson = new Gson();
        return gson.toJson(notes);
    }

    public static Note NoteFromJson (String json){
        Gson gson = new Gson();
        return gson.fromJson(json,Note.class);
    }

    public static ObservableList<Note> NotesFromJson (String json){
        Gson gson = new Gson();
        Type listType = new TypeToken<ObservableList<Note>>(){}.getType();
        return gson.fromJson(json,listType);
    }
}
