package model;

@SuppressWarnings("unused")
public abstract class Customer {
    private int custID;
    private String custType;
    private String custName;
    private String custAddress;
    private String custPostalCode;
    private String custPhoneNumber;
    private String custCountry;
    private String custStateProvince;

    public Customer(int custID, String custType, String custName, String custAddress, String custPostalCode,
                    String custPhoneNumber, String custCountry, String custStateProvince) {
        this.custID = custID;
        this.custType = custType;
        this.custName = custName;
        this.custAddress = custAddress;
        this.custPostalCode = custPostalCode;
        this.custPhoneNumber = custPhoneNumber;
        this.custCountry = custCountry;
        this.custStateProvince = custStateProvince;
    }

    public void setCustID(int custID) {this.custID = custID;}
    public void setCustType(String custType) {this.custType = custType;}
    public void setCustName(String custName) {this.custName = custName;}
    public void setCustAddress(String custAddress) {this.custAddress = custAddress;}
    public void setCustPostalCode(String custPostalCode) {this.custPostalCode = custPostalCode;}
    public void setCustPhoneNumber(String custPhoneNumber) {this.custPhoneNumber = custPhoneNumber;}
    public void setCustCountry(String custCountry) {this.custCountry = custCountry;}
    public void setCustStateProvince(String custStateProvince) {this.custStateProvince = custStateProvince;}

    public int getCustID() {return custID;}
    public String getCustType() {return custType;}
    public String getCustName() {return custName;}
    public String getCustAddress() {return custAddress;}
    public String getCustPostalCode() {return custPostalCode;}
    public String getCustPhoneNumber() {return custPhoneNumber;}
    public String getCustCountry() {return custCountry;}
    public String getCustStateProvince() {return custStateProvince;}
}