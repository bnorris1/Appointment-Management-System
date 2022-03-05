package model;
import java.sql.Timestamp;

@SuppressWarnings("unused")
public class Appointment {
    private int apptID;
    private String apptTitle;
    private String apptDescription;
    private String apptLocation;
    private String apptType;
    private Timestamp apptStartDateTime;
    private Timestamp apptEndDateTime;
    private String apptCustName;
    private String apptContactName;
    private int apptCustID;

    public Appointment(int apptID,
                       String apptTitle,
                       String apptDescription,
                       String apptLocation,
                       String apptType,
                       Timestamp apptStartDateTime,
                       Timestamp apptEndDateTime,
                       String apptCustName,
                       String apptContactName,
                       int apptCustID) {
        this.apptID = apptID;
        this.apptTitle = apptTitle;
        this.apptDescription = apptDescription;
        this.apptLocation = apptLocation;
        this.apptType = apptType;
        this.apptStartDateTime = apptStartDateTime;
        this.apptEndDateTime = apptEndDateTime;
        this.apptCustName = apptCustName;
        this.apptContactName = apptContactName;
        this.apptCustID = apptCustID;
    }

    public void setApptID(int apptID) {this.apptID = apptID;}
    public void setApptTitle(String apptTitle) {this.apptTitle = apptTitle;}
    public void setApptDescription(String apptDescription) {this.apptDescription = apptDescription;}
    public void setApptLocation(String apptLocation) {this.apptLocation = apptLocation;}
    public void setApptType(String apptType) {this.apptType = apptType;}
    public void setApptStartDateTime(Timestamp apptStartDateTime) {this.apptStartDateTime = apptStartDateTime;}
    public void setApptEndDateTime(Timestamp apptEndDateTime) {this.apptEndDateTime = apptEndDateTime;}
    public void setApptCustName(String apptCustName) {this.apptCustName = apptCustName;}
    public void setApptContactName(String apptContactName) {this.apptContactName = apptContactName;}
    public void setApptCustID(int apptCustID) {this.apptCustID = apptCustID;}

    public int getApptID() {return apptID;}
    public String getApptTitle() {return apptTitle;}
    public String getApptDescription() {return apptDescription;}
    public String getApptLocation() {return apptLocation;}
    public String getApptType() {return apptType;}
    public Timestamp getApptStartDateTime() {return apptStartDateTime;}
    public Timestamp getApptEndDateTime() {return apptEndDateTime;}
    public String getApptCustName() {return apptCustName;}
    public String getApptContactName() {return apptContactName;}
    public int getApptCustID() {return apptCustID;}
}
