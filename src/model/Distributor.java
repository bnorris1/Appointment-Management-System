package model;

@SuppressWarnings("unused")
public class Distributor extends Customer {

    private int accountNumber;

    public Distributor(int custID, String custType, String custName, String custAddress, String custPostalCode,
                       String custPhoneNumber, String custCountry, String custStateProvince, int accountNumber) {
        super (custID, custType, custName, custAddress, custPostalCode, custPhoneNumber, custCountry, custStateProvince);
        this.accountNumber = accountNumber;
    }

    public void setAccountNumber(int accountNumber) {this.accountNumber = accountNumber;}

    public int getAccountNumber() {return accountNumber;}
}