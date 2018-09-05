package memo.memoapp.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import memo.memoapp.model.Note;
import memo.memoapp.model.ObservableList;
import memo.memoapp.serialization.Injector;
import memo.memoapp.view.HomeView;

public class HomeActivity extends AppCompatActivity {

    private HomeView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retrieve notes
        ObservableList<Note> notes = Injector.getNoteDAO(this).retrieveAllNotes();

        // create view
        view = new HomeView(LayoutInflater.from(this),this,notes);
        notes.addObserver(view);

        setContentView(view.getRoot());
    }

    public void goToNewNote(){
        startActivity(new Intent(this,AddNoteActivity.class));
    }

    public void viewNote(Note note){
        Intent intent = new Intent(this,AddNoteActivity.class);
        intent.putExtra(AddNoteActivity.EDIT_NOTE_MODE,note);
        startActivity(intent);
    }

    public void removeNote(Note note){
        Injector.getNoteDAO(this).remove(note);
    }
}
