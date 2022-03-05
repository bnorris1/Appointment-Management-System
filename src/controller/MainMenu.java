package controller;
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
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

@SuppressWarnings("unused")
public class MainMenu implements Initializable {

    private Stage stage;
    private Parent scene;
    @FXML private ToggleGroup apptViewTG;
    @FXML private RadioButton allView;
    @FXML private RadioButton monthView;
    @FXML private RadioButton weekView;
    @FXML private TextField custSearchTF;
    @FXML private TableView<Customer> custTBView;
    @FXML private TableColumn<Customer, Integer> custIDTBV;
    @FXML private TableColumn<Customer, String> custNameTBV;
    @FXML private TableColumn<Customer, String> custAddressTBV;
    @FXML private TableColumn<Customer, String> custPostalCodeTBV;
    @FXML private TableColumn<Customer, String> custPhoneNumberTBV;
    @FXML private TableColumn<Customer, String> custCountryTBV;
    @FXML private TableColumn<Customer, String> custStateProvinceTBV;
    @FXML private TableView<Appointment> apptTBView;
    @FXML private TableColumn<Appointment, Integer> apptIDTBV;
    @FXML private TableColumn<Appointment, String> apptTitleTBV;
    @FXML private TableColumn<Appointment, String> apptDescriptionTBV;
    @FXML private TableColumn<Appointment, String> apptLocationTBV;
    @FXML private TableColumn<Appointment, String> apptTypeTBV;
    @FXML private TableColumn<Appointment, LocalDateTime> apptStartDateTimeTBV;
    @FXML private TableColumn<Appointment, LocalDateTime> apptEndDateTimeTBV;
    @FXML private TableColumn<Appointment, String> apptCustNameTBV;
    @FXML private TableColumn<Appointment, String> apptContactTBV;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        custTBView.setItems(JDBQueries.getAllCustomersTBV());
        custIDTBV.setCellValueFactory(new PropertyValueFactory<>("custID"));
        custNameTBV.setCellValueFactory(new PropertyValueFactory<>("custName"));
        custAddressTBV.setCellValueFactory(new PropertyValueFactory<>("custAddress"));
        custPostalCodeTBV.setCellValueFactory(new PropertyValueFactory<>("custPostalCode"));
        custPhoneNumberTBV.setCellValueFactory(new PropertyValueFactory<>("custPhoneNumber"));
        custCountryTBV.setCellValueFactory(new PropertyValueFactory<>("custCountry"));
        custStateProvinceTBV.setCellValueFactory(new PropertyValueFactory<>("custStateProvince"));
        apptTBView.setItems(JDBQueries.getAllAppointmentsTBV());
        apptIDTBV.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        apptTitleTBV.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
        apptDescriptionTBV.setCellValueFactory(new PropertyValueFactory<>("apptDescription"));
        apptLocationTBV.setCellValueFactory(new PropertyValueFactory<>("apptLocation"));
        apptTypeTBV.setCellValueFactory(new PropertyValueFactory<>("apptType"));
        apptStartDateTimeTBV.setCellValueFactory(new PropertyValueFactory<>("apptStartDateTime"));
        apptEndDateTimeTBV.setCellValueFactory(new PropertyValueFactory<>("apptEndDateTime"));
        apptCustNameTBV.setCellValueFactory(new PropertyValueFactory<>("apptCustName"));
        apptContactTBV.setCellValueFactory(new PropertyValueFactory<>("apptContactName"));
        apptViewTG.selectedToggleProperty().addListener((obs, oldSelection, newSelection) ->
        {if (newSelection != null) {
            if (newSelection == allView){apptTBView.setItems(JDBQueries.loadAppointmentTBV(1));}
            if (newSelection == weekView){apptTBView.setItems(JDBQueries.loadAppointmentTBV(2));}
            if (newSelection == monthView){apptTBView.setItems(JDBQueries.loadAppointmentTBV(3));}
        }});
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
                    custTBView.setPlaceholder(new Label("No Customer ID match found"));}}
        } else {
            foundCust = Shared.lookupCust(custSearch);
            custTBView.setItems(foundCust);
            if (foundCust.isEmpty()){
                custTBView.setPlaceholder(new Label("No Customer Name match found"));}}
    }

    @FXML private void onActionNavToAddCust(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddCustomer.fxml")));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    @FXML private void onActionNavToUpdateCust(ActionEvent event) throws IOException {
        if (custTBView.getSelectionModel().isEmpty()) {Shared.errorManager("m1");}
        else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/UpdateCustomer.fxml"));
            loader.load();
            UpdateCustomer controller = loader.getController();
            Customer custToUpdate = custTBView.getSelectionModel().getSelectedItem();

            if (custToUpdate.getCustType().equals("Distributor")){
                controller.receiveCustomer((Distributor) custToUpdate);
            } else {
                controller.receiveCustomer((Individual) custToUpdate);
            }

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.centerOnScreen();
            stage.show();
        }
    }

    @FXML private void onActionDeleteCust(ActionEvent event) {
        if (custTBView.getSelectionModel().isEmpty()) {Shared.errorManager("m2");}
        else {
            JDBQueries.deleteCustomer(custTBView.getSelectionModel().getSelectedItem().getCustID());
            custTBView.getSelectionModel().clearSelection();
            custTBView.setItems(JDBQueries.getAllCustomersTBV());
            apptTBView.setItems(JDBQueries.getAllAppointmentsTBV());
            Shared.errorManager("m3");
        }
    }

    @FXML private void onActionNavToAddAppt(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddAppointment.fxml")));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    @FXML private void onActionNavToUpdateAppt(ActionEvent event) throws IOException {
        if (apptTBView.getSelectionModel().isEmpty()) {Shared.errorManager("m4");}
        else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/UpdateAppointment.fxml"));
            loader.load();
            UpdateAppointment controller = loader.getController();
            controller.receiveAppointment(apptTBView.getSelectionModel().getSelectedItem());
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.centerOnScreen();
            stage.show();}
    }

    @FXML private void onActionDeleteAppt(ActionEvent event) {
        if (apptTBView.getSelectionModel().isEmpty()) {Shared.errorManager("m5");}
        else {
            JDBQueries.deleteAppointment(apptTBView.getSelectionModel().getSelectedItem().getApptID());
            apptTBView.getSelectionModel().clearSelection();
            apptTBView.setItems(JDBQueries.getAllAppointmentsTBV());
            Shared.errorManager("m6");
        }
    }

    @FXML private void onActionExit(ActionEvent event) {Shared.exitDialog();}

    @FXML private void onActionNavToApptReport(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ApptReport.fxml")));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    @FXML private void onActionNavToContactReport(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ContactReport.fxml")));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }

    @FXML private void onActionNavToRegionReport(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/RegionReport.fxml")));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }
}