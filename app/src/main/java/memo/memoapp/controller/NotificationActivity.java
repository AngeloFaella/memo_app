package memo.memoapp.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;

import java.util.Date;

import memo.memoapp.R;
import memo.memoapp.model.Reminder;
import memo.memoapp.view.notification.NotificationView;
import memo.memoapp.view.notification.ReminderScheduler;

public class NotificationActivity extends AppCompatActivity {

    public static final String NOTIFICATION_TITLE = "TITLE";
    public static final String EDIT_MODE = "EDIT_MODE";
    public static final String REMINDER = "REMINDER_TO_SHOW";
    public static final String NOTIFICATION_SCHEDULED = "SCHEDULED";
    public static final String NOTIFICATION_CANCELED = "CANCELED";

    private Reminder reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String title = getIntent().getStringExtra(NOTIFICATION_TITLE);

        NotificationView view;
        if(getIntent().hasExtra(EDIT_MODE)){
            reminder = (Reminder) getIntent().getSerializableExtra(REMINDER);
            view = new NotificationView(LayoutInflater.from(this), this, reminder);
        }else {
            view = new NotificationView(LayoutInflater.from(this), this, title);
        }

        setContentView(view.getRoot());
    }

    public void exit(Date date, String title){

        // create request code
        int code;
        if(reminder != null) code = reminder.getReqCode(); // replace reminder
        else code =  title.hashCode();

        // create new reminder
        reminder = new Reminder(date,code,title);

        // create result intent
        Intent result = new Intent();
        result.putExtra(NOTIFICATION_SCHEDULED,reminder);
        setResult(RESULT_OK,result);
        finish();
    }

    public void exitWithReminderDeleted(){
        // create result intent
        Intent result = new Intent();
        result.putExtra(NOTIFICATION_CANCELED,true);
        setResult(RESULT_OK,result);
        finish();
    }
}
