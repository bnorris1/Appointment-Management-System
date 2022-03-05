package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.cReport;
import utilities.JDBQueries;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.ResourceBundle;

@SuppressWarnings("FieldCanBeLocal")
public class ContactReport implements Initializable {

    private Stage stage;
    private Parent scene;
    @FXML private TableView<cReport> reportTBV;
    @FXML private TableColumn<cReport, String> contact;
    @FXML private TableColumn<cReport, Integer> apptID;
    @FXML private TableColumn<cReport, String> title;
    @FXML private TableColumn<cReport, String> type;
    @FXML private TableColumn<cReport, String> description;
    @FXML private TableColumn<cReport, Timestamp> start;
    @FXML private TableColumn<cReport, Timestamp> end;
    @FXML private TableColumn<cReport, String> custID;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        reportTBV.setItems(JDBQueries.getContactReport());
        contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        apptID.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        start.setCellValueFactory(new PropertyValueFactory<>("start"));
        end.setCellValueFactory(new PropertyValueFactory<>("end"));
        custID.setCellValueFactory(new PropertyValueFactory<>("custID"));
    }

    @FXML private void onActionNavToMainPage(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainMenu.fxml")));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }
}
