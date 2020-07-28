    //Bakitas Theofanis icsd15133 Askisi 2i 
import java.io.Serializable;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Fanis
 */
public class Announcement implements Serializable {
    // attributes
   private String Title;
   private String Description;
   private String Announcer;
   private String Announcement_date;

    public Announcement(String Title, String Description, String Announcer, String Announcement_date) { // constructor
        this.Title = Title;
        this.Description = Description;
        this.Announcer = Announcer;
        this.Announcement_date = Announcement_date;
    }
    //accessors 
    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public String getAnnouncer() {
        return Announcer;
    }

    public String getAnnouncement_date() {
        return Announcement_date;
    }

    //printingInfo
    @Override
    public String toString() {
        return "Announcement{" + "Title=" + Title + ", Description=" + Description + ", Announcer=" + Announcer + ", Announcement_date=" + Announcement_date + '}';
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public void setAnnouncer(String Announcer) {
        this.Announcer = Announcer;
    }

    public void setAnnouncement_date(String Announcement_date) {
        this.Announcement_date = Announcement_date;
    }
   
   
   
}
