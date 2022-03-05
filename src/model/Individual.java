package model;

@SuppressWarnings("unused")
public class Individual extends Customer {

    private String emailAddress;

    public Individual(int custID, String custType, String custName, String custAddress, String custPostalCode,
                      String custPhoneNumber, String custCountry, String custStateProvince, String emailAddress) {
        super (custID, custType, custName, custAddress, custPostalCode, custPhoneNumber, custCountry, custStateProvince);
        this.emailAddress = emailAddress;
    }

    public void setEmailAddress(String emailAddress) {this.emailAddress = emailAddress;}

    public String getEmailAddress() {return emailAddress;}
}
