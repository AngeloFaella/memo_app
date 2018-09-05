package memo.memoapp.view.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import memo.memoapp.R;
import memo.memoapp.controller.AddNoteActivity;
import memo.memoapp.controller.HomeActivity;

/**
 * Created by Angelo Faella
 */

public class NotificationBuilder extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 123;
    public static final String NOTIFICATION_TITLE = "note";
    private static final String LOG_REMINDER = "Memo Notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(LOG_REMINDER, "Alarm Received");
        String name = "";
        if(intent.hasExtra(NOTIFICATION_TITLE)) name = intent.getStringExtra(NOTIFICATION_TITLE);
        showNotification(context,name);
    }

    public static void showNotification(Context context, String name) {

        // get notification ringtone
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // create intent
        Intent notificationIntent = new Intent(context, AddNoteActivity.class);
        notificationIntent.putExtra(AddNoteActivity.NOTIFICATION_MODE,name);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // create a back stack for the activity
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(new Intent(context, HomeActivity.class));
        stackBuilder.addNextIntent(notificationIntent);

        // get the pending intent
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(
                NOTIFICATION_ID,PendingIntent.FLAG_UPDATE_CURRENT);

        // create notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notification = builder.setContentTitle(context.getString(R.string.memo))
                .setContentText(name).setAutoCancel(true)
                .setSound(alarmSound).setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent).build();

        // show notification
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID,notification);

        Log.e(LOG_REMINDER, "Notification shown");
    }
}
