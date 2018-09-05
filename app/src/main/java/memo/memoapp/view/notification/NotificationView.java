package memo.memoapp.view.notification;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import memo.memoapp.R;
import memo.memoapp.controller.NotificationActivity;
import memo.memoapp.model.Reminder;
import memo.memoapp.view.AbstractView;
import memo.memoapp.view.DatePicker;
import memo.memoapp.view.TimePicker;

/**
 * Created by Angelo Faella
 */

public class NotificationView extends AbstractView {

    private String notificationTitle;
    private int year, month, day, hour, min;
    private TextView time, date, title;
    private String mode="default";

    public NotificationView(LayoutInflater inflater, AppCompatActivity controller, String title) {
        super(inflater, controller);
        notificationTitle = title;
        init();
    }

    public NotificationView(LayoutInflater inflater, AppCompatActivity controller, Reminder reminder) {
        super(inflater, controller);
        notificationTitle = reminder.getTitle();
        mode = NotificationActivity.EDIT_MODE;
        init();

        // fill info
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reminder.getDate());
        date.setText(calendar.get(Calendar.DAY_OF_MONTH)+"/"+ (calendar.get(Calendar.MONTH)+1)+"/"+ calendar.get(Calendar.YEAR));
        time.setText(calendar.get(Calendar.HOUR_OF_DAY) +":"+ calendar.get(Calendar.MINUTE));
        title.setText(reminder.getTitle());
    }

    private void init(){
        root = inflater.inflate(R.layout.activity_notification,null);

        // init Toolbar
        final Toolbar myToolbar=(Toolbar)root.findViewById(R.id.toolbar_notification);
        controller.setSupportActionBar(myToolbar);
        controller.getSupportActionBar().setDisplayShowTitleEnabled(false);
        // activity title
        TextView name = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        name.setText(R.string.set_notification);
        // on back pressed
        View back = myToolbar.findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.onBackPressed();
            }
        });

        // init layouts & TextViews
        View timeLayout = root.findViewById(R.id.layout_time);
        View dateLayout = root.findViewById(R.id.layout_date);
        //View titleLayout = root.findViewById(R.id.layout_title);

        time = (TextView) root.findViewById(R.id.time_value);
        date = (TextView) root.findViewById(R.id.date_value);
        title = (TextView) root.findViewById(R.id.title_value);
        title.setText(notificationTitle);

        // create Pickers
        createDatePicker(dateLayout, date);
        createTimePicker(timeLayout, time);

        // fab DONE
        FloatingActionButton done = (FloatingActionButton) root.findViewById(R.id.fab_notification_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set date
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,day,hour,min);

                ((NotificationActivity)controller).exit(calendar.getTime(),notificationTitle);
            }
        });

        // fab DELETE
        if(mode.equals(NotificationActivity.EDIT_MODE)) {
            FloatingActionButton cancel = (FloatingActionButton) root.findViewById(R.id.fab_notification_canceled);
            cancel.setVisibility(View.VISIBLE);
            // on click
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirm();
                }
            });
        }
    }


    private void createDatePicker(View action, final TextView result){

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker picker = new DatePicker();
                picker.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int y, int m, int dayOfMonth) {
                        year = y;
                        month = m;
                        day = dayOfMonth;
                        result.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                });
                picker.show(controller.getSupportFragmentManager(), "datePicker");
            }
        });
    }

    private void createTimePicker(View action, final TextView result){

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker picker = new TimePicker();
                picker.setTimeListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                        hour = hourOfDay;
                        min = minute;
                        result.setText(hourOfDay+":"+minute);
                    }
                });
                picker.show(controller.getSupportFragmentManager(), "datePicker");
            }
        });
    }

    private void confirm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(controller);

        // title & message
        builder.setTitle(R.string.warning);
        builder.setMessage(R.string.alert_delete_memo);

        // on click listeners
        builder.setPositiveButton(R.string.answer_delete_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((NotificationActivity) controller).exitWithReminderDeleted();
            }
        }).setNegativeButton("No", null);

        builder.create().show();
    }
}
