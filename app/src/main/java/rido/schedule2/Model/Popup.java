package rido.schedule2.Model;

import android.graphics.Bitmap;
import com.framgia.library.calendardayview.data.IPopup;
import java.util.Calendar;


public class Popup implements IPopup {

    public Calendar timeStart;
    public Calendar timeEnd;

    private String imageStart;
    private String imageEnd;


    private String title;

    private String description;

    private String quote;

    public Popup() {

    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    @Override
    public String getQuote() {
        return quote;
    }

    public void setImageStart(String imageStart) {
        this.imageStart = imageStart;
    }

    @Override
    public String getImageStart() {
        return imageStart;
    }

    public void setImageEnd(String imageEnd) {
        this.imageEnd = imageEnd;
    }

    @Override
    public String getImageEnd() {
        return imageEnd;
    }

    @Override
    public Boolean isAutohide() {
        return false;
    }

   /* public void setStartTime(Calendar timeStart) {
        this.timeStart = timeStart;
    }*/

    @Override
    public Calendar getStartTime() {
        return timeStart;
    }


    public void setEndTime(Calendar timeEnd) {
        this.timeEnd = timeEnd ;
    }

    @Override
    public Calendar getEndTime() {
        return timeEnd;
    }

    public void setStartTime(Calendar timeStart) {
        this.timeStart = timeStart;
    }
}