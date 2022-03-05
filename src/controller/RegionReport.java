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
import model.rReport;
import utilities.JDBQueries;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

@SuppressWarnings("FieldCanBeLocal")
public class RegionReport implements Initializable {

    private Stage stage;
    private Parent scene;
    @FXML private TableView<rReport> reportTBV;
    @FXML private TableColumn<rReport, String> region;
    @FXML private TableColumn<rReport, Integer> number;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        reportTBV.setItems(JDBQueries.getRegionReport());
        region.setCellValueFactory(new PropertyValueFactory<>("region"));
        number.setCellValueFactory(new PropertyValueFactory<>("number"));

    }

    @FXML private void onActionNavToMainPage(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainMenu.fxml")));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.show();
    }
}
