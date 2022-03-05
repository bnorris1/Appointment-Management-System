package model;
import java.sql.Timestamp;

@SuppressWarnings("unused")
public class cReport {

    private String contact;
    private int apptID;
    private String title;
    private String type;
    private String description;
    private Timestamp start;
    private Timestamp end;
    private String custID;

    public cReport(String contact,
                   int apptID,
                   String title,
                   String type,
                   String description,
                   Timestamp start,
                   Timestamp end,
                   String custID) {
        this.contact = contact;
        this.apptID = apptID;
        this.title = title;
        this.type = type;
        this.description = description;
        this.start = start;
        this.end = end;
        this.custID = custID;
    }

    public void setContact(String contact) {this.contact = contact;}
    public void setApptID(int apptID) {this.apptID = apptID;}
    public void setTitle(String title) {this.title = title;}
    public void setType(String type) {this.type = type;}
    public void setDescription(String description) {this.description = description;}
    public void setStart(Timestamp start) {this.start = start;}
    public void setEnd(Timestamp end) {this.end = end;}
    public void setCustID(String custID) {this.custID = custID;}

    public String getContact() {return contact;}
    public int getApptID() {return apptID;}
    public String getTitle() {return title;}
    public String getType() {return type;}
    public String getDescription() {return description;}
    public Timestamp getStart() {return start;}
    public Timestamp getEnd() {return end;}
    public String getCustID() {return custID;}
}
