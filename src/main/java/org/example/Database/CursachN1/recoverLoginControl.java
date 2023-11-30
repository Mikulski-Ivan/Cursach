package org.example.Database.CursachN1;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForFX.Scenes;

import java.net.URL;
import java.util.ResourceBundle;

import static org.example.Database.CursachN1.checkForCorrectSymbols.isCorrectSymbolsForMail;
import static org.example.Database.CursachN1.checkForCorrectSymbols.isCorrectSymbolsForPasswordAndLogin;

public class recoverLoginControl implements Initializable {
    @FXML
    private Label errorLabel;

    @FXML
    private Button backButton;

    @FXML
    private TextField mailField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button sentCodeButton;

    final DatabaseHandler dbHandler = new DatabaseHandler();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(actionEvent -> {
            Scenes.MENU.setScene((Stage) backButton.getScene().getWindow());
        });

        sentCodeButton.setDisable(true);

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() != 0) {
                if (isCorrectSymbolsForPasswordAndLogin(newValue)) {
                    passwordField.setText(oldValue);
                }
                sentCodeButton.setDisable(passwordField.getText().isEmpty() || mailField.getText().isEmpty() || newValue.length()<6);
            } else sentCodeButton.setDisable(true);
        });

        mailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() != 0) {
                if (isCorrectSymbolsForMail(newValue)) {
                    mailField.setText(oldValue);
                }
                sentCodeButton.setDisable(passwordField.getText().isEmpty() || mailField.getText().isEmpty());
            } else sentCodeButton.setDisable(true);
        });

        sentCodeButton.setOnAction(actionEvent -> sentCode());
    }

    public void sentCode() {
        User user = new User();
        user.setPassword(passwordField.getText());
        user.setMail(mailField.getText());

        user = dbHandler.isUserWithPasswordAndMail(user);
        if (user!=null) {
            String ID= user.getID();
            String mail= user.getMail();
            dbHandler.insertRecoverOnlineUser(Const.USERS_AND_ONLINE_USER_LOGIN,ID, mail);

            Scenes.SENT_CODE.setScene((Stage) sentCodeButton.getScene().getWindow());
        } else errorLabel.setText(Const.NO_SUCH_USER);
    }
}
