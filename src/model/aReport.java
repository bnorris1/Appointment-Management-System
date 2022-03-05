package model;

@SuppressWarnings("unused")
public class aReport {

    private String month;
    private String type;
    private int number;

    public aReport(String month, String type, int number) {
        this.month = month;
        this.type = type;
        this.number = number;
    }

    public void setMonth(String month) {this.month = month;}
    public void setType(String type) {this.type = type;}
    public void setNumber(int number) {this.number = number;}

    public String getMonth() {return month;}
    public String getType() {return type;}
    public int getNumber() {return number;}
}
