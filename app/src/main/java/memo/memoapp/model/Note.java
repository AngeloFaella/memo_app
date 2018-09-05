package memo.memoapp.model;


import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Angelo Faella
 */

public class Note implements Serializable{

    private String title;
    private Date date;
    private String content;
    private Category category;

    private Reminder reminder;


    public Note(String title, Date date, String content, Category category) {
        this.title = title;
        this.date = date;
        this.content = content;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public Date getDate() {
        return date;
    }

    public String getDateFormat() {
        if(date == null) return "";

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR);
    }

    public void setDate(Date date) {
        this.date = date;
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public boolean hasReminder() {
        if(reminder == null) return false;

        // remove old reminder
        Date now = new Date();
        if(reminder.getDate().before(now)){
            reminder = null;
            return false;
        }

        return true;
    }



    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }



    @Override
    public String toString(){
        return title;
    }

    @Override
    public boolean equals(Object o){
        if(! o.getClass().equals(this.getClass())) return false;

        Note n = (Note) o;
        return this.getTitle().equals(n.getTitle()) && this.getCategory().equals(n.getCategory());
    }



    public Reminder getReminder() {
        return reminder;
    }

    public void setReminder(Reminder reminder) {
        this.reminder = reminder;
    }
}
