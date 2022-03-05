package controller;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;
import model.Distributor;
import model.Individual;
import model.Shared;
import utilities.JDBQueries;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


@SuppressWarnings("unused")
public class UpdateCustomer implements Initializable {

    private Stage stage;
    private Parent scene;
    @FXML private ComboBox<String> custType;
    @FXML private Label changingLabel;
    @FXML private TextField custExtra;
    @FXML private TextField custID;
    @FXML private TextField custName;
    @FXML private TextField custAddress;
    @FXML private TextField custPostalCode;
    @FXML private TextField custPhoneNumber;
    @FXML private ComboBox<String> custCountry;
    @FXML private ComboBox<String> custStateProvince;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        custType.setItems(FXCollections.observableArrayList("Distributor", "Individual"));
    }

    void receiveCustomer (Distributor custToUpdate){
        custID.setText(String.valueOf(custToUpdate.getCustID()));
        custType.getSelectionModel().select(custToUpdate.getCustType());
        custExtra.setText(String.valueOf(custToUpdate.getAccountNumber()));
        custName.setText(custToUpdate.getCustName());
        custAddress.setText(custToUpdate.getCustAddress());
        custPostalCode.setText(custToUpdate.getCustPostalCode());
        custPhoneNumber.setText(custToUpdate.getCustPhoneNumber());
        custCountry.setItems(JDBQueries.getAllCountries());
        custCountry.setValue(custToUpdate.getCustCountry());
        custStateProvince.setItems(JDBQueries.getRelatedFirstLevels(custCountry.getValue()));
        custStateProvince.setValue(custToUpdate.getCustStateProvince());
        if (custType.getSelectionModel().isSelected(0)) {changingLabel.setText("Account Number");}
        else {changingLabel.setText("Email Address");}
    }

    void receiveCustomer (Individual custToUpdate){
        custID.setText(String.valueOf(custToUpdate.getCustID()));
        custType.getSelectionModel().select(custToUpdate.getCustType());
        custExtra.setText(custToUpdate.getEmailAddress());
        custName.setText(custToUpdate.getCustName());
        custAddress.setText(custToUpdate.getCustAddress());
        custPostalCode.setText(custToUpdate.getCustPostalCode());
        custPhoneNumber.setText(custToUpdate.getCustPhoneNumber());
        custCountry.setItems(JDBQueries.getAllCountries());
        custCountry.setValue(custToUpdate.getCustCountry());
        custStateProvince.setItems(JDBQueries.getRelatedFirstLevels(custCountry.getValue()));
        custStateProvince.setValue(custToUpdate.getCustStateProvince());
        if (custType.getSelectionModel().isSelected(0)) {changingLabel.setText("Account Number");}
        else {changingLabel.setText("Email Address");}
    }

    @FXML private void onActionCustTypeSelected(ActionEvent actionEvent) {
        if (custType.getSelectionModel().isSelected(0)) {changingLabel.setText("Account Number");}
        else {changingLabel.setText("Email Address");}
    }

    @FXML private void onActionCountrySelected(ActionEvent actionEvent) {
        custStateProvince.setItems(JDBQueries.getRelatedFirstLevels(custCountry.getValue()));
    }

    @FXML private void onActionUpdateCust(ActionEvent event) throws IOException {
        String cType = custType.getSelectionModel().getSelectedItem();
        if (custType.getSelectionModel().isEmpty()){Shared.errorManager("c7");}
        else if (cType.equals("Distributor") && !Shared.isInteger(custExtra.getText())) {Shared.errorManager("c8");}
        else if (cType.equals("Individual") && !custExtra.getText().contains("@") ) {Shared.errorManager("c9");}
        else if (custName.getText().isEmpty()){Shared.errorManager("c1");}
        else if (custAddress.getText().isEmpty()){Shared.errorManager("c2");}
        else if (custPostalCode.getText().isEmpty()){Shared.errorManager("c3");}
        else if (custPhoneNumber.getText().isEmpty()){Shared.errorManager("c4");}
        else if (custCountry.getSelectionModel().isEmpty()) {Shared.errorManager("c5");}
        else if (custStateProvince.getSelectionModel().isEmpty()) {Shared.errorManager("c6");}
        else {
            int cID = Integer.parseInt(custID.getText());
            String cName = custName.getText();
            String cAddress = custAddress.getText();
            String cPostalCode = custPostalCode.getText();
            String cPhoneNumber = custPhoneNumber.getText();
            String cCountry = custCountry.getSelectionModel().getSelectedItem();
            String cStateProvince = custStateProvince.getSelectionModel().getSelectedItem();
            Customer customer;

            if (cType.equals("Distributor")) {
                int cAccountNumber = Integer.parseInt(custExtra.getText());
                customer = new Distributor(cID,cType,cName,cAddress,cPostalCode,cPhoneNumber,cCountry,cStateProvince,cAccountNumber);
                JDBQueries.updateCustomer((Distributor) customer);
            } else {
                String cEmailAddress = custExtra.getText();
                customer = new Individual(cID,cType,cName,cAddress,cPostalCode,cPhoneNumber,cCountry,cStateProvince,cEmailAddress);
                JDBQueries.updateCustomer((Individual) customer);
            }

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainMenu.fxml")));
            stage.setScene(new Scene(scene));
            stage.centerOnScreen();
            stage.show();
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