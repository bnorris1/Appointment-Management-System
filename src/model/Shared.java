package model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import utilities.JDBConnection;
import utilities.JDBQueries;
import java.util.Optional;

public abstract class Shared {

    public static String userName;
    public static int foundInteger;

    public static boolean isInteger(String searchTerm) {
        try {foundInteger = Integer.parseInt(searchTerm);}
        catch (NumberFormatException e) {return false;}
        return true;
    }

    public static ObservableList<Customer> lookupCust(String custName){
        ObservableList<Customer> foundCust = FXCollections.observableArrayList();
        ObservableList<Customer> custPool = JDBQueries.getAllCustomersTBV();
        for (Customer cust : custPool){
            if (cust.getCustName().toLowerCase().contains(custName.toLowerCase())){
                foundCust.add(cust);
            }
        }
        return foundCust;
    }

    public static Customer lookupCust(int custId)  {
        ObservableList<Customer> custPool = JDBQueries.getAllCustomersTBV();
        for (Customer cust : custPool){
            if (cust.getCustID() == custId){return cust;}
        }
        return null;
    }

    public static void errorManager(String errorCode){
        String errorText = "";
        switch (errorCode){
            case "a1": errorText = "Please select a Contact."; break;
            case "a2": errorText = "Please select a Customer."; break;
            case "a3": errorText = "Please provide a Title."; break;
            case "a4": errorText = "Please provide a Description."; break;
            case "a5": errorText = "Please provide a Location."; break;
            case "a6": errorText = "Please provide a Type."; break;
            case "a7": errorText = "Please select a Start Date."; break;
            case "a8": errorText = "Please select a Start Time."; break;
            case "a9": errorText = "Please select an End Date."; break;
            case "a10": errorText = "Please select an End Time."; break;
            case "a11": errorText = "Cannot have overlapping appointments."; break;
            case "a12": errorText = "Please select a Start Date then a Timeslot."; break;
            case "a13": errorText = "Please select an End Date then a Timeslot."; break;
            case "a14": errorText = "Start date cannot be earlier than today.\nDate has automatically been set to today's date."; break;
            case "a15": errorText = "Please select a Start Date first."; break;
            case "a16": errorText = "End date cannot be earlier than the Start date."; break;
            case "a17": errorText = "Please select an End Time that is AFTER the Start Time."; break;
            case "a18": errorText = "Appointment successfully updated."; break;
            case "c1": errorText = "Please provide a Customer Name."; break;
            case "c2": errorText = "Please provide a Customer Address."; break;
            case "c3": errorText = "Please provide a Postal Code."; break;
            case "c4": errorText = "Please provide a Phone Number."; break;
            case "c5": errorText = "Please select a Country."; break;
            case "c6": errorText = "Please select a State or Province."; break;
            case "c7": errorText = "Please select a Customer Type."; break;
            case "c8": errorText = "Only numbers are allowed for Account Number."; break;
            case "c9": errorText = "Please enter an Email Address."; break;
            case "l1": errorText = "Please provide a User Name."; break;
            case "l2": errorText = "Please provide a Password."; break;
            case "l3": errorText = "Incorrect User Name and Password combination."; break;
            case "m1": errorText = "Please select a Customer to update."; break;
            case "m2": errorText = "Please select a Customer to delete."; break;
            case "m3": errorText = "Customer has been deleted.\nAll appointments for this customer have been deleted."; break;
            case "m4": errorText = "Please select an Appointment to update."; break;
            case "m5": errorText = "Please select an Appointment to delete."; break;
            case "m6": errorText = "Appointment has been deleted."; break;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("SalesTech");
        alert.setHeaderText(null);
        alert.setContentText(errorText);
        alert.showAndWait();
    }

    public static void exitDialog(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("SalesTech");
        alert.setHeaderText(null);
        alert.setContentText("Please confirm that you want to exit the program.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(null) == ButtonType.OK) {
            JDBConnection.closeConnection();
            System.exit(0);
        }
    }
}