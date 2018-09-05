package memo.memoapp.view.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import memo.memoapp.model.Reminder;
import memo.memoapp.view.AbstractView;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Angelo Faella
 */

public class ReminderScheduler {


    public static boolean setReminder(Context context, Reminder reminder) {

        // set date
        Calendar now = Calendar.getInstance();
        Calendar date = Calendar.getInstance();
        date.setTime(reminder.getDate());
        date.set(Calendar.SECOND,0);

        // invalid date
        if(date.before(now)){
            Log.e(AbstractView.LOG_VIEW,"Reminder NOT scheduled, invalid date");
            return false;
        }

        // enable a receiver
        ComponentName receiver = new ComponentName(context, NotificationBuilder.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        // set Intent
        Intent intent = new Intent(context, NotificationBuilder.class);
        intent.putExtra(NotificationBuilder.NOTIFICATION_TITLE,reminder.getTitle());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                reminder.getReqCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // create and set the Alarm Manager
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(),pendingIntent);


        Log.e(AbstractView.LOG_VIEW,"Reminder scheduled successfully: "+date.getTime()+"\nReq code:"+reminder.getReqCode());
        return true;
    }


    public static void cancelReminder(Context context, Reminder reminder) {


        // create intent
        Intent intent = new Intent(context, NotificationBuilder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                reminder.getReqCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // cancel alarm
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        //pendingIntent.cancel();

        Log.e(AbstractView.LOG_VIEW,"Reminder canceled: "+reminder.getReqCode());
    }
}
