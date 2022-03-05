package model;

@SuppressWarnings("unused")
public class rReport {

    private String region;
    private int number;

    public rReport(String region, int number){
        this.region = region;
        this.number = number;
    }

    public void setRegion(String region) {this.region = region;}
    public void setNumber(int number) {this.number = number;}

    public String getRegion() {return region;}
    public int getNumber() {return number;}
}
