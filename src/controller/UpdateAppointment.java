package controller;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.*;
import utilities.JDBQueries;
import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

@SuppressWarnings("unused")
public class UpdateAppointment implements Initializable {

    private Stage stage;
    private Parent scene;
    @FXML private ComboBox<String> contact;
    @FXML private TextField custSearchTF;
    @FXML private TableView<Customer> custTBView;
    @FXML private TableColumn<Customer, Integer> custIDTBV;
    @FXML private TableColumn<Customer, String> custNameTBV;
    @FXML private TextField custID;
    @FXML private TextField custName;
    @FXML private TextField custAddress;
    @FXML private TextField custPostalCode;
    @FXML private TextField custPhoneNumber;
    @FXML private TextField custCountry;
    @FXML private TextField custStateProvince;
    @FXML private TextField apptID;
    @FXML private TextField appTitle;
    @FXML private TextField appDescription;
    @FXML private TextField appLocation;
    @FXML private TextField appType;
    @FXML private DatePicker appStartDatePicker;
    @FXML private DatePicker appEndDatePicker;
    @FXML private TableView<String> startTimeslotTBV;
    @FXML private TableColumn<String, String> startTimeCompanyCol;
    @FXML private TableView<String> endTimeslotTBV;
    @FXML private TableColumn<String, String> endTimeCompanyCol;
    @FXML private TextField startTimeslotTF;
    @FXML private TextField endTimeslotTF;
    private final ObservableList<String> timeslotList = FXCollections.observableArrayList();
    private final ZoneId zoneId = ZoneId.of("America/New_York");
    private ZonedDateTime startZDT;
    private ZonedDateTime endZDT;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        contact.setItems(JDBQueries.getAllContacts());
        custTBView.setItems(JDBQueries.getAllCustomersTBV());
        custIDTBV.setCellValueFactory(new PropertyValueFactory<>("custID"));
        custNameTBV.setCellValueFactory(new PropertyValueFactory<>("custName"));
        buildTimeslotList();
        startTimeslotTBV.setItems(timeslotList);
        startTimeCompanyCol.setCellValueFactory(index -> new ReadOnlyStringWrapper(index.getValue()));
        endTimeslotTBV.setItems(timeslotList);
        endTimeCompanyCol.setCellValueFactory(index -> new ReadOnlyStringWrapper(index.getValue()));

        custTBView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                receiveCustomer(custTBView.getSelectionModel().getSelectedItem());
            }
        });

        appStartDatePicker.valueProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection.isBefore(LocalDate.now())) {
                appStartDatePicker.setValue(LocalDate.now());
                Shared.errorManager("a14");
            }
            startTimeslotTBV.getSelectionModel().clearSelection();
            startTimeslotTF.setText("Automatically Populated");
            appEndDatePicker.setValue(null);
            endTimeslotTBV.getSelectionModel().clearSelection();
            endTimeslotTF.setText("Automatically Populated");
        });

        appEndDatePicker.valueProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                endTimeslotTBV.getSelectionModel().clearSelection();
                endTimeslotTF.setText("Automatically Populated");
                if (appStartDatePicker.getValue() == null){
                    appEndDatePicker.setValue(null);
                    Shared.errorManager("a15");
                }
                if (appStartDatePicker.getValue() != null && appStartDatePicker.getValue().compareTo(appEndDatePicker.getValue()) > 0) {
                    appEndDatePicker.setValue(null);
                    Shared.errorManager("a16");
                }
            }
        });
    }

    void receiveAppointment (Appointment apptToUpdate){
        contact.getSelectionModel().select(apptToUpdate.getApptContactName());
        custTBView.getSelectionModel().select(getCustomerByID(apptToUpdate));
        receiveCustomer(custTBView.getSelectionModel().getSelectedItem());
        apptID.setText(String.valueOf(apptToUpdate.getApptID()));
        appTitle.setText(apptToUpdate.getApptTitle());
        appDescription.setText(apptToUpdate.getApptDescription());
        appLocation.setText(apptToUpdate.getApptLocation());
        appType.setText(apptToUpdate.getApptType());
        appStartDatePicker.setValue(apptToUpdate.getApptStartDateTime().toLocalDateTime().toLocalDate());
        appEndDatePicker.setValue(apptToUpdate.getApptEndDateTime().toLocalDateTime().toLocalDate());
        startTimeslotTF.setText(ZonedDateTime.of(apptToUpdate.getApptStartDateTime().toLocalDateTime(),zoneId).format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss z")));
        endTimeslotTF.setText(ZonedDateTime.of(apptToUpdate.getApptEndDateTime().toLocalDateTime(),zoneId).format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss z")));
        startZDT = ZonedDateTime.parse(startTimeslotTF.getText(),DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss z"));
        endZDT = ZonedDateTime.parse(endTimeslotTF.getText(),DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss z"));
    }

    private int getCustomerByID (Appointment appointmentHasCustomer){
        int index = 0;
        ObservableList<Customer> customerList = JDBQueries.getAllCustomersTBV();
        for (Customer customer : customerList){
            if (customer.getCustID() == appointmentHasCustomer.getApptCustID()){
                index = customerList.indexOf(customer);
            }
        }
        return index;
    }

    private void receiveCustomer (Customer custForAppt){
        custID.setText(String.valueOf(custForAppt.getCustID()));
        custName.setText(custForAppt.getCustName());
        custAddress.setText(custForAppt.getCustAddress());
        custPostalCode.setText(custForAppt.getCustPostalCode());
        custPhoneNumber.setText(custForAppt.getCustPhoneNumber());
        custCountry.setText(custForAppt.getCustCountry());
        custStateProvince.setText(custForAppt.getCustStateProvince());
    }

    private void buildTimeslotList(){
        for (int i = 8 ; i < 22 ; ++i) {
            if (i < 10) {
                timeslotList.add("0"+i+":00:00 EST");
                timeslotList.add("0"+i+":15:00 EST");
                timeslotList.add("0"+i+":30:00 EST");
                timeslotList.add("0"+i+":45:00 EST");
            } else {
                timeslotList.add(i+":00:00 EST");
                timeslotList.add(i+":15:00 EST");
                timeslotList.add(i+":30:00 EST");
                timeslotList.add(i+":45:00 EST");
                if (i == 21){timeslotList.add("22:00:00 EST");}
            }
        }
    }

    @FXML private void custSearch(KeyEvent event) {
        String custSearch = custSearchTF.getText();
        ObservableList<Customer> custPool = JDBQueries.getAllCustomersTBV();
        ObservableList<Customer> foundCust = FXCollections.observableArrayList();
        if (Shared.isInteger(custSearch)) {
            for (Customer cust : custPool) {
                if (cust.getCustID() == Integer.parseInt(custSearch)) {
                    foundCust.add(Shared.lookupCust(Shared.foundInteger));
                    custTBView.setItems(foundCust);
                } else {
                    foundCust.removeAll();
                    custTBView.setItems(foundCust);
                    custTBView.setPlaceholder(new Label("No Customer ID match found"));
                }
            }
        } else {
            foundCust = Shared.lookupCust(custSearch);
            custTBView.setItems(foundCust);
            if (foundCust.isEmpty()){
                custTBView.setPlaceholder(new Label("No Customer Name match found"));
            }
        }
    }

    @FXML private void onActionAddStartTimeslot(ActionEvent event) {
        if (appStartDatePicker.getValue() == null){Shared.errorManager("a7");}
        else if (startTimeslotTBV.getSelectionModel().isEmpty()){Shared.errorManager("a12");}
        else {
            LocalDate startLocalDate = LocalDate.parse(appStartDatePicker.getValue().toString());
            LocalTime startLocalTime = LocalTime.parse(startTimeslotTBV.getSelectionModel().getSelectedItem().substring(0,8));
            startZDT = ZonedDateTime.of(startLocalDate,startLocalTime,zoneId);
            startTimeslotTF.setText(startZDT.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss z")));
            appEndDatePicker.setValue(null);
            endTimeslotTBV.getSelectionModel().clearSelection();
            endTimeslotTF.setText("Automatically Populated");
        }
    }

    @FXML private void onActionAddEndTimeslot(ActionEvent event) {
        if (appEndDatePicker.getValue() == null){Shared.errorManager("a9");}
        else if (endTimeslotTBV.getSelectionModel().isEmpty()){Shared.errorManager("a13");}
        else if (appStartDatePicker.getValue().compareTo(appEndDatePicker.getValue()) == 0 && startTimeslotTBV.getSelectionModel().getSelectedIndex() >= endTimeslotTBV.getSelectionModel().getSelectedIndex()){Shared.errorManager("a17");}
        else {
            LocalDate endLocalDate = LocalDate.parse(appEndDatePicker.getValue().toString());
            LocalTime endLocalTime = LocalTime.parse(endTimeslotTBV.getSelectionModel().getSelectedItem().substring(0,8));
            endZDT = ZonedDateTime.of(endLocalDate,endLocalTime,zoneId);
            endTimeslotTF.setText(endZDT.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss z")));
        }
    }

    @FXML private void onActionSaveAppointment(ActionEvent event) throws IOException {
        if (contact.getSelectionModel().isEmpty()) {Shared.errorManager("a1");}
        else if (custID.getText().equals("Automatically Populated")) {Shared.errorManager("a2");}
        else if (appTitle.getText().isEmpty()){Shared.errorManager("a3");}
        else if (appDescription.getText().isEmpty()){Shared.errorManager("a4");}
        else if (appLocation.getText().isEmpty()){Shared.errorManager("a5");}
        else if (appType.getText().isEmpty()){Shared.errorManager("a6");}
        else if (appStartDatePicker.getValue() == null) {Shared.errorManager("a7");}
        else if (startTimeslotTF.getText().equals("Automatically Populated")) {Shared.errorManager("a8");}
        else if (appEndDatePicker.getValue() == null) {Shared.errorManager("a9");}
        else if (endTimeslotTF.getText().equals("Automatically Populated")) {Shared.errorManager("a10");}
        else {
            String custID2 = custID.getText();
            int apptID2 = Integer.parseInt(apptID.getText());
            if (JDBQueries.overlaps(startZDT,endZDT,custID2,apptID2)){Shared.errorManager("a11");}
            else {
                String contactID = JDBQueries.getContactIDfromName(contact.getValue());
                JDBQueries.updateAppointment(
                        apptID2,
                        appTitle.getText(),
                        appDescription.getText(),
                        appLocation.getText(),
                        appType.getText(),
                        startZDT,
                        endZDT,
                        custID2,
                        contactID);
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainMenu.fxml")));
                stage.setScene(new Scene(scene));
                stage.centerOnScreen();
                stage.show();
                Shared.errorManager("a18");
            }
        }
    }

    @FXML private void onActionNavToMainPage(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainMenu.fxml")));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }
}