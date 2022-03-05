package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Shared;
import utilities.JDBQueries;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class Login implements Initializable {

    private Stage stage;
    private Parent scene;
    @FXML private TextField userName;
    @FXML private PasswordField password;

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {}

    @FXML private void onActionLogin(ActionEvent event) throws IOException {
        if (userName.getText().isEmpty()) {Shared.errorManager("l1");}
        else if (password.getText().isEmpty()) {Shared.errorManager("l2");}
        else {
            if (JDBQueries.authUser(userName.getText(), password.getText())) {
                Shared.userName = userName.getText();
                JDBQueries.prepareDB();
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainMenu.fxml")));
                stage.setScene(new Scene(scene));
                stage.centerOnScreen();
                stage.show();}
            else {Shared.errorManager("l3");}
        }
    }

    @FXML private void onActionExit(ActionEvent event) {Shared.exitDialog();}
}