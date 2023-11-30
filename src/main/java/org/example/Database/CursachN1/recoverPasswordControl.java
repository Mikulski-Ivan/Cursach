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

public class recoverPasswordControl implements Initializable {
    @FXML
    private Label errorLabel;

    @FXML
    private Button backButton;

    @FXML
    private TextField mailField;

    @FXML
    private TextField loginField;

    @FXML
    private Button sentCodeButton;

    private final DatabaseHandler dbHandler = new DatabaseHandler();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(actionEvent -> {
            Scenes.MENU.setScene((Stage) backButton.getScene().getWindow());
        });

        sentCodeButton.setDisable(true);

        loginField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() != 0) {
                if (isCorrectSymbolsForPasswordAndLogin(newValue)) {
                    loginField.setText(oldValue);
                }
                sentCodeButton.setDisable(loginField.getText().isEmpty() || mailField.getText().isEmpty() || newValue.length()<6);
            } else sentCodeButton.setDisable(true);
        });

        mailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() != 0) {
                if (isCorrectSymbolsForMail(newValue)) {
                    mailField.setText(oldValue);
                }
                sentCodeButton.setDisable(loginField.getText().isEmpty() || mailField.getText().isEmpty());
            } else sentCodeButton.setDisable(true);
        });

        sentCodeButton.setOnAction(actionEvent -> sentCode());
    }

    public void sentCode() {
        User user = new User();
        user.setLogin(loginField.getText());
        user.setMail(mailField.getText());

        user = dbHandler.findUserByLoginAndMail(user);
        if (user != null) {
            User finalUser = user;
            String ID = finalUser.getID();
            String mail = finalUser.getMail();
            dbHandler.insertRecoverOnlineUser(Const.USERS_AND_ONLINE_USER_PASSWORD, ID, mail);

            Scenes.SENT_CODE.setScene((Stage) sentCodeButton.getScene().getWindow());
        } else errorLabel.setText(Const.NO_SUCH_USER);
    }
}
