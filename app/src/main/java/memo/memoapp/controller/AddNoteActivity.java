package memo.memoapp.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import memo.memoapp.model.Note;
import memo.memoapp.model.Reminder;
import memo.memoapp.serialization.Injector;
import memo.memoapp.serialization.NoteDAO;
import memo.memoapp.view.AddNoteView;
import memo.memoapp.view.notification.ReminderScheduler;

public class AddNoteActivity extends AppCompatActivity {

    public static final String EDIT_NOTE_MODE = "EDIT NOTE";
    public static final String NOTIFICATION_MODE = "NOTIFICATION NOTE";

    public static final int NOTIFICATION_REQUEST = 789;
    public static final int REQ_CODE_SPEECH_INPUT = 100;

    private AddNoteView view;
    private Note note;
    private Reminder reminder;

    private Runnable saveChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        if(intent.hasExtra(EDIT_NOTE_MODE)){
            // EDIT NOTE
            note = (Note) intent.getSerializableExtra(EDIT_NOTE_MODE);
            view = new AddNoteView(LayoutInflater.from(this),this,note);
        }else if(intent.hasExtra(NOTIFICATION_MODE)) { // here from notification
            // retrieve note
            note = Injector.getNoteDAO(this).retrieve(intent.getStringExtra(NOTIFICATION_MODE));
            note.setReminder(null);
            view = new AddNoteView(LayoutInflater.from(this),this,note);
        }else{
            // NEW NOTE
            view = new AddNoteView(LayoutInflater.from(this),this);
        }

        setContentView(view.getRoot());
    }

    public boolean saveOrUpdateNote(Note note) {
        NoteDAO dao =  Injector.getNoteDAO(this);
        // if already exists, replace with the updated note
        if(this.note != null) dao.remove(this.note);

        // save changes related to the reminder of this note
        if(saveChanges != null){
            saveChanges.run();
            note.setReminder(reminder);
        }

        // save
        return dao.save(note);
    }


    @Override
    public void onBackPressed(){
        view.safeExit();
    }

    public void finish(){
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE_SPEECH_INPUT){
            if(resultCode == RESULT_OK){
                view.setSpeechInputResult(requestCode,resultCode,data);
            }
        }

        if(requestCode == NOTIFICATION_REQUEST){
            if(resultCode == RESULT_OK){
                Log.e(Service.LOG_TAG,"NotificationActivity: result OK");

                // add reminder
                if(data.hasExtra(NotificationActivity.NOTIFICATION_SCHEDULED)) {
                    reminder = (Reminder) data.getSerializableExtra(NotificationActivity.NOTIFICATION_SCHEDULED);
                    saveChanges = new Runnable() {
                        @Override
                        public void run() {
                            ReminderScheduler.setReminder(AddNoteActivity.this,reminder);
                        }
                    };
                }else{  // cancel reminder
                    saveChanges = new Runnable() {
                        @Override
                        public void run() {
                            ReminderScheduler.cancelReminder(AddNoteActivity.this,reminder);
                            reminder = null;
                        }
                    };
                }
                view.setChanged(true);
            }
        }

    }


    public void goToNotificationView(String title) {
        Intent intent = new Intent(this,NotificationActivity.class);
        intent.putExtra(NotificationActivity.NOTIFICATION_TITLE , title);

        //Reminder already exists
        if( (note != null && note.hasReminder()) || reminder != null) {
            intent.putExtra(NotificationActivity.EDIT_MODE, true);

            if(reminder == null) reminder = note.getReminder();
            intent.putExtra(NotificationActivity.REMINDER,reminder);
        }

        startActivityForResult(intent,NOTIFICATION_REQUEST);
    }

}
