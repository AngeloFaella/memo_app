package memo.memoapp.model;

import android.app.PendingIntent;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Angelo Faella
 */

public class Reminder implements Serializable{

    private Date date;
    private int reqCode;
    private String title;

    public Reminder(Date date, int reqCode, String title) {
        this.date = date;
        this.reqCode = reqCode;
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public int getReqCode() {
        return reqCode;
    }

    public String getTitle() {
        return title;
    }

    public static Reminder copyOf(Reminder reminder){
        if(reminder == null) return null;
        return new Reminder(reminder.getDate(),reminder.getReqCode(),reminder.getTitle());
    }

}
