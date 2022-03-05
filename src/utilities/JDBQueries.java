package utilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public abstract class JDBQueries {

    public static boolean authUser(String userNameGiven, String passWordGiven){
        boolean hasPermission = false;
        String hashedPassword = "";
        userNameGiven = userNameGiven.toLowerCase();
        passWordGiven = passWordGiven.toLowerCase();
        try {
            byte[] hash = MessageDigest.getInstance("SHA-512").digest(passWordGiven.getBytes(StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            for (int i: hash) {stringBuilder.append(String.format("%02x", 0XFF & i));}
            hashedPassword = stringBuilder.toString();
        }
        catch (Exception e) {e.printStackTrace();}

        try {
            PreparedStatement ps = JDBConnection.connection.prepareStatement(
                    "SELECT * FROM users WHERE User_Name = ? AND Password = ?");
            ps.setString(1, userNameGiven);
            ps.setString(2, hashedPassword);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {hasPermission = true;}
            rs.close();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
        return hasPermission;
    }

    public static boolean overlaps(ZonedDateTime start, ZonedDateTime end, String custID, int apptID){
        LocalDateTime convertedStartDateTimeString = start.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime convertedEndDateTimeString = end.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        boolean overlaps = false;
        try {
            PreparedStatement ps = JDBConnection.connection.prepareStatement(
                    "SELECT * FROM appointments WHERE (Start <= ? AND End >= ?) AND Customer_ID = ? AND Appointment_ID != ?");
            ps.setString(1, convertedEndDateTimeString.toString());
            ps.setString(2, convertedStartDateTimeString.toString());
            ps.setString(3, custID);
            ps.setInt(4, apptID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {overlaps = true;}
            rs.close();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
        return overlaps;
    }

    public static boolean overlaps(ZonedDateTime start, ZonedDateTime end, String custID){
        LocalDateTime convertedStartDateTimeString = start.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime convertedEndDateTimeString = end.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        boolean overlaps = false;
        try {
            PreparedStatement ps = JDBConnection.connection.prepareStatement(
                    "SELECT * FROM appointments WHERE (Start <= ? AND End >= ?) AND Customer_ID = ?");
            ps.setString(1, convertedEndDateTimeString.toString());
            ps.setString(2, convertedStartDateTimeString.toString());
            ps.setString(3, custID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {overlaps = true;}
            rs.close();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
        return overlaps;
    }

    public static ObservableList<Customer> getAllCustomersTBV(){
        ObservableList<Customer> customerListTBV = FXCollections.observableArrayList();
        try {
            ResultSet rs = JDBConnection.connection.prepareStatement(
                    "SELECT a.Customer_ID,a.Customer_Name,a.Address,a.Postal_Code,a.Phone,c.Country,b.Division,a.Subclass " +
                    "FROM customers a " +
                    "INNER JOIN first_level_divisions b ON a.Division_ID=b.Division_ID " +
                    "INNER JOIN countries c ON b.Country_ID=c.Country_ID " +
                    "ORDER BY Customer_ID").executeQuery();
            while (rs.next()){
                int cID = rs.getInt(1);
                String cName = rs.getString(2);
                String cAddress = rs.getString(3);
                String cPostalCode = rs.getString(4);
                String cPhoneNumber = rs.getString(5);
                String cCountry = rs.getString(6);
                String cStateProvince = rs.getString(7);
                String cSubclass = rs.getString(8);
                if (Shared.isInteger(cSubclass)) {
                    String cType = "Distributor";
                    int cAccountNumber = Integer.parseInt(cSubclass);
                    customerListTBV.add(new Distributor(cID,cType,cName,cAddress,cPostalCode,cPhoneNumber,cCountry,cStateProvince,cAccountNumber));
                }
                else {
                    String cType = "Individual";
                    customerListTBV.add(new Individual(cID,cType,cName,cAddress,cPostalCode,cPhoneNumber,cCountry,cStateProvince,cSubclass));
                }
            }
            rs.close();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
        return customerListTBV;
    }

    public static ObservableList<Appointment> getAllAppointmentsTBV(){
        ObservableList<Appointment> appointmentListTBV = FXCollections.observableArrayList();
        try {
            ResultSet rs = JDBConnection.connection.prepareStatement(
                    "SELECT a.Appointment_ID,a.Title,a.Description,a.Location,a.Type,a.Start,a.End,c.Customer_Name,b.Contact_Name,a.Customer_ID " +
                    "FROM appointments a " +
                    "INNER JOIN contacts b ON a.Contact_ID = b.Contact_ID " +
                    "INNER JOIN customers c ON a.Customer_ID = c.Customer_ID " +
                    "ORDER BY Appointment_ID").executeQuery();
            while (rs.next()){
                int apptID = rs.getInt(1);
                String apptTitle = rs.getString(2);
                String apptDescription = rs.getString(3);
                String apptLocation = rs.getString(4);
                String apptType = rs.getString(5);
                Timestamp apptStartDateTime = rs.getTimestamp(6);
                Timestamp apptEndDateTime = rs.getTimestamp(7);
                String apptCustName = rs.getString(8);
                String apptContactName = rs.getString(9);
                int apptCustID = rs.getInt(10);
                Appointment pulledAppointment = new Appointment(apptID, apptTitle, apptDescription, apptLocation,apptType,apptStartDateTime,apptEndDateTime,apptCustName,apptContactName,apptCustID);
                appointmentListTBV.add(pulledAppointment);
            }
            rs.close();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
        return appointmentListTBV;
    }

    public static ObservableList<Appointment> getWeeklyAppointmentsTBV(){
        ObservableList<Appointment> appointmentListTBV = FXCollections.observableArrayList();
        try {
            ResultSet rs = JDBConnection.connection.prepareStatement(
                    "SELECT a.Appointment_ID,a.Title,a.Description,a.Location,a.Type,a.Start,a.End,c.Customer_Name,b.Contact_Name,a.Customer_ID " +
                    "FROM appointments a " +
                    "INNER JOIN contacts b ON a.Contact_ID = b.Contact_ID " +
                    "INNER JOIN customers c ON a.Customer_ID = c.Customer_ID " +
                    "ORDER BY Appointment_ID").executeQuery();
            while (rs.next()){
                if (!rs.getTimestamp(7).toLocalDateTime().isBefore(LocalDateTime.now()) && rs.getTimestamp(7).toLocalDateTime().isBefore(LocalDateTime.now().plusWeeks(1))) {
                    int apptID = rs.getInt(1);
                    String apptTitle = rs.getString(2);
                    String apptDescription = rs.getString(3);
                    String apptLocation = rs.getString(4);
                    String apptType = rs.getString(5);
                    Timestamp apptStartDateTime = rs.getTimestamp(6);
                    Timestamp apptEndDateTime = rs.getTimestamp(7);
                    String apptCustName = rs.getString(8);
                    String apptContactName = rs.getString(9);
                    int apptCustID = rs.getInt(10);
                    Appointment pulledAppointment = new Appointment(apptID, apptTitle, apptDescription, apptLocation,apptType,apptStartDateTime,apptEndDateTime,apptCustName,apptContactName,apptCustID);
                    appointmentListTBV.add(pulledAppointment);
                }
            }
            rs.close();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
        return appointmentListTBV;
    }

    public static ObservableList<Appointment> getMonthlyAppointmentsTBV(){
        ObservableList<Appointment> appointmentListTBV = FXCollections.observableArrayList();
        try {
            ResultSet rs = JDBConnection.connection.prepareStatement(
                    "SELECT a.Appointment_ID,a.Title,a.Description,a.Location,a.Type,a.Start,a.End,c.Customer_Name,b.Contact_Name,a.Customer_ID " +
                    "FROM appointments a " +
                    "INNER JOIN contacts b ON a.Contact_ID = b.Contact_ID " +
                    "INNER JOIN customers c ON a.Customer_ID = c.Customer_ID " +
                    "ORDER BY Appointment_ID").executeQuery();
            while (rs.next()){
                if (!rs.getTimestamp(7).toLocalDateTime().isBefore(LocalDateTime.now()) && rs.getTimestamp(7).toLocalDateTime().isBefore(LocalDateTime.now().plusMonths(1))) {
                    int apptID = rs.getInt(1);
                    String apptTitle = rs.getString(2);
                    String apptDescription = rs.getString(3);
                    String apptLocation = rs.getString(4);
                    String apptType = rs.getString(5);
                    Timestamp apptStartDateTime = rs.getTimestamp(6);
                    Timestamp apptEndDateTime = rs.getTimestamp(7);
                    String apptCustName = rs.getString(8);
                    String apptContactName = rs.getString(9);
                    int apptCustID = rs.getInt(10);
                    Appointment pulledAppointment = new Appointment(apptID, apptTitle, apptDescription, apptLocation,apptType,apptStartDateTime,apptEndDateTime,apptCustName,apptContactName,apptCustID);
                    appointmentListTBV.add(pulledAppointment);
                }
            }
            rs.close();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
        return appointmentListTBV;
    }

    public static ObservableList<Appointment> loadAppointmentTBV(int selection){
        ObservableList<Appointment> temp = FXCollections.observableArrayList();
        switch (selection){
            case 1:
                temp = JDBQueries.getAllAppointmentsTBV();
                break;
            case 2:
                temp = JDBQueries.getWeeklyAppointmentsTBV();
                break;
            case 3:
                temp = JDBQueries.getMonthlyAppointmentsTBV();
                break;
        }
        return temp;
    }

    public static ObservableList<String> getAllCountries (){
        ObservableList<String> countries = FXCollections.observableArrayList();
        try {
            ResultSet rs = JDBConnection.connection.prepareStatement(
                    "SELECT Country FROM countries ORDER BY Country").executeQuery();
            while (rs.next()){
                String country = rs.getString(1);
                countries.add(country);
            }
            rs.close();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
        return countries;
    }

    public static ObservableList<String> getRelatedFirstLevels (String custCountryToFilter){
        ObservableList<String> relatedFirstLevels = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = JDBConnection.connection.prepareStatement(
                    "SELECT Division FROM first_level_divisions a " +
                    "JOIN countries b ON a.Country_ID=b.Country_ID " +
                    "WHERE b.Country = ? ORDER BY Division");
            ps.setString(1, custCountryToFilter);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String relatedFirstLevel = rs.getString(1);
                relatedFirstLevels.add(relatedFirstLevel);
            }
            rs.close();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
        return relatedFirstLevels;
    }

    public static ObservableList<String> getAllContacts (){
        ObservableList<String> contacts = FXCollections.observableArrayList();
        try {
            ResultSet rs = JDBConnection.connection.prepareStatement(
                    "SELECT Contact_Name FROM contacts ORDER BY Contact_Name").executeQuery();
            while (rs.next()){
                String contact = rs.getString(1);
                contacts.add(contact);
            }
            rs.close();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
        return contacts;
    }

    public static ObservableList<aReport> getApptReport(){
        ObservableList<aReport> aReport = FXCollections.observableArrayList();
        try {
            ResultSet rs = JDBConnection.connection.prepareStatement(
                    "SELECT MONTHNAME(Start) AS Month, Type, COUNT(*) " +
                    "FROM appointments " +
                    "GROUP BY Month, Type").executeQuery();
            while (rs.next()){
                String month = rs.getString(1);
                String type = rs.getString(2);
                int number = rs.getInt(3);
                aReport entry = new aReport(month, type, number);
                aReport.add(entry);
            }
            rs.close();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
        return aReport;
    }

    public static ObservableList<cReport> getContactReport(){
        ObservableList<cReport> cReport = FXCollections.observableArrayList();
        try {
            ResultSet rs = JDBConnection.connection.prepareStatement(
                    "SELECT a.Contact_Name,b.Appointment_ID,b.Title,b.Type,b.Description,b.Start,b.End,b.Customer_ID " +
                    "FROM contacts a INNER JOIN appointments b ON a.Contact_ID=b.Contact_ID ORDER BY Contact_Name").executeQuery();
            while (rs.next()){
                String contact = rs.getString(1);
                int apptID = rs.getInt(2);
                String title = rs.getString(3);
                String type = rs.getString(4);
                String description = rs.getString(5);
                Timestamp start = rs.getTimestamp(6);
                Timestamp end = rs.getTimestamp(7);
                String custID = rs.getString(8);
                cReport entry = new cReport(contact, apptID, title,type,description,start,end,custID);
                cReport.add(entry);
            }
            rs.close();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
        return cReport;
    }

    public static ObservableList<rReport> getRegionReport(){
        ObservableList<rReport> rReport = FXCollections.observableArrayList();
        try {
            ResultSet rs = JDBConnection.connection.prepareStatement(
                    "SELECT a.Country, COUNT(*) FROM countries a " +
                    "INNER JOIN first_level_divisions c ON a.Country_ID=c.Country_ID " +
                    "INNER JOIN customers d ON c.Division_ID=d.Division_ID " +
                    "INNER JOIN appointments b ON b.Customer_ID=d.Customer_ID " +
                    "GROUP BY Country").executeQuery();
            while (rs.next()){
                String region = rs.getString(1);
                int number = rs.getInt(2);
                rReport entry = new rReport(region,number);
                rReport.add(entry);
            }
            rs.close();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
        return rReport;
    }

    public static String convertCustStateProvinceToDivisionID (String custStateProvince){
        String divisionID = null;
        try {
            PreparedStatement ps = JDBConnection.connection.prepareStatement(
                    "SELECT Division_ID FROM first_level_divisions WHERE Division = ?");
            ps.setString(1, custStateProvince);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){divisionID = rs.getString(1);}
            rs.close();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
        return divisionID;
    }

    public static String getContactIDfromName (String contactName){
        String contactID = null;
        try {
            PreparedStatement ps = JDBConnection.connection.prepareStatement(
                    "SELECT Contact_ID FROM contacts WHERE Contact_Name = ?");
            ps.setString(1, contactName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){contactID = rs.getString(1);}
            rs.close();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
        return contactID;
    }

    public static void prepareDB(){
        try{JDBConnection.connection.prepareStatement ("SET GLOBAL time_zone = '+00:00'").execute();}
        catch (SQLException throwables) {throwables.printStackTrace();}
        try {JDBConnection.connection.prepareStatement("SET SESSION time_zone ='+00:00'").execute();}
        catch (SQLException throwables) {throwables.printStackTrace();}
    }

    public static void addCustomer(Distributor distributor) {
        int id = distributor.getCustID();
        String name = distributor.getCustName();
        String address = distributor.getCustAddress();
        String postalCode = distributor.getCustPostalCode();
        String phoneNumber = distributor.getCustPhoneNumber();
        String divisionID = convertCustStateProvinceToDivisionID(String.valueOf(distributor.getCustStateProvince()));
        int accountNumber = distributor.getAccountNumber();
        try{
            PreparedStatement ps = JDBConnection.connection.prepareStatement (
                    "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID, Subclass) " +
                    "VALUES (?,?,?,?,?,?,?)");
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, address);
            ps.setString(4, postalCode);
            ps.setString(5, phoneNumber);
            ps.setString(6, divisionID);
            ps.setInt(7, accountNumber);
            ps.execute();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
    }

    public static void addCustomer(Individual individual) {
        int id = individual.getCustID();
        String name = individual.getCustName();
        String address = individual.getCustAddress();
        String postalCode = individual.getCustPostalCode();
        String phoneNumber = individual.getCustPhoneNumber();
        String divisionID = convertCustStateProvinceToDivisionID(String.valueOf(individual.getCustStateProvince()));
        String emailAddress = individual.getEmailAddress();
        try{
            PreparedStatement ps = JDBConnection.connection.prepareStatement (
                    "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID, Subclass) " +
                    "VALUES (?,?,?,?,?,?,?)");
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, address);
            ps.setString(4, postalCode);
            ps.setString(5, phoneNumber);
            ps.setString(6, divisionID);
            ps.setString(7, emailAddress);
            ps.execute();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
    }

    public static void updateCustomer(Distributor distributor){
        String name = distributor.getCustName();
        String address = distributor.getCustAddress();
        String postalCode = distributor.getCustPostalCode();
        String phoneNumber = distributor.getCustPhoneNumber();
        String divisionID = convertCustStateProvinceToDivisionID(String.valueOf(distributor.getCustStateProvince()));
        int accountNumber = distributor.getAccountNumber();
        int id = distributor.getCustID();
        try{
            PreparedStatement ps = JDBConnection.connection.prepareStatement (
                    "UPDATE customers " +
                    "SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ?, Subclass = ? " +
                    "WHERE Customer_ID = ?");
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phoneNumber);
            ps.setString(5, divisionID);
            ps.setInt(6, accountNumber);
            ps.setInt(7, id);
            ps.execute();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
    }

    public static void updateCustomer(Individual individual){
        int id = individual.getCustID();
        String name = individual.getCustName();
        String address = individual.getCustAddress();
        String postalCode = individual.getCustPostalCode();
        String phoneNumber = individual.getCustPhoneNumber();
        String divisionID = convertCustStateProvinceToDivisionID(String.valueOf(individual.getCustStateProvince()));
        String emailAddress = individual.getEmailAddress();
        try{
            PreparedStatement ps = JDBConnection.connection.prepareStatement (
                    "UPDATE customers " +
                    "SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ?, Subclass = ? " +
                    "WHERE Customer_ID = ?");
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phoneNumber);
            ps.setString(5, divisionID);
            ps.setString(6, emailAddress);
            ps.setInt(7, id);
            ps.execute();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
    }

    public static void deleteCustomer(int customerID){
        try{
            PreparedStatement ps = JDBConnection.connection.prepareStatement (
                    "DELETE FROM appointments WHERE Customer_ID = ?");
            ps.setInt(1, customerID);
            ps.execute();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
        try{
            PreparedStatement ps = JDBConnection.connection.prepareStatement (
                    "DELETE FROM customers WHERE Customer_ID = ?");
            ps.setInt(1, customerID);
            ps.execute();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
    }

    public static void addAppointment(String title, String description, String location, String type, ZonedDateTime startDateTime, ZonedDateTime endDateTime, String custID, String contactID){
        LocalDateTime convertedStartDateTimeString = startDateTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime convertedEndDateTimeString = endDateTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        try{
            PreparedStatement ps = JDBConnection.connection.prepareStatement (
                    "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, Contact_ID) " +
                    "VALUES (?,?,?,?,?,?,?,?)");
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setString(5, convertedStartDateTimeString.toString());
            ps.setString(6, convertedEndDateTimeString.toString());
            ps.setString(7, custID);
            ps.setString(8, contactID);
            ps.execute();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
    }

    public static void updateAppointment(int appID, String title, String description, String location, String type, ZonedDateTime startDateTime, ZonedDateTime endDateTime, String custID, String contactID){
        LocalDateTime convertedStartDateTimeString = startDateTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime convertedEndDateTimeString = endDateTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        try{
            PreparedStatement ps = JDBConnection.connection.prepareStatement (
                    "UPDATE appointments "  +
                    "SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Customer_ID=?, Contact_ID=? " +
                    "WHERE Appointment_ID=?");
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setString(5, convertedStartDateTimeString.toString());
            ps.setString(6, convertedEndDateTimeString.toString());
            ps.setString(7, custID);
            ps.setString(8, contactID);
            ps.setInt(9, appID);
            ps.execute();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
    }

    public static void deleteAppointment(int appointmentID){
        try{
            PreparedStatement ps = JDBConnection.connection.prepareStatement (
                    "DELETE FROM appointments WHERE Appointment_ID = ?");
            ps.setInt(1, appointmentID);
            ps.execute();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
    }

    public static void apptSuccess (){
        String custName = null;
        String apptID = null;
        String apptType = null;
        try {
            ResultSet rs = JDBConnection.connection.prepareStatement(
                    "SELECT b.Customer_Name,a.Appointment_ID,a.Type " +
                    "FROM appointments a " +
                    "INNER JOIN customers b ON a.Customer_ID = b.Customer_ID " +
                    "WHERE a.Appointment_ID=(SELECT max(Appointment_ID) FROM appointments)").executeQuery();
            while (rs.next()){
                custName = rs.getString(1);
                apptID = rs.getString(2);
                apptType = rs.getString(3);
            }
            rs.close();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("SalesTech");
        alert.setHeaderText(null);
        alert.setContentText("Appointment created for "+custName+".\nAppointment ID = "+apptID+"\nAppointment Type = "+apptType);
        alert.showAndWait();
    }

    public static int getNextAvailCustId () {
        int custID = 0;
        try {
            ResultSet rs = JDBConnection.connection.prepareStatement(
                    "SELECT MAX(Customer_ID) FROM customers").executeQuery();
            while (rs.next()){
                custID = (rs.getInt(1) + 1);
            }
            rs.close();
        }
        catch (SQLException throwables) {throwables.printStackTrace();}
        return custID;
    }
}






