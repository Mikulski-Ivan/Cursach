package org.example.Database.CursachN1;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForFX.Scenes;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class entranceControl implements Initializable {

    @FXML
    private Button recoverLoginButton;
    @FXML
    private Button recoverPasswordButton;
    @FXML
    private Button backButton;
    @FXML
    private Button entranceButton;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passField;
    @FXML
    private Label incorrectParametersLabel;
    final DatabaseHandler dbHandler = new DatabaseHandler();
    public void authorisation() {
        String login = loginField.getText();
        String password = passField.getText();

        User user = new User();
        user.setLogin(login);
        ResultSet foundUser = dbHandler.findUser(user);
        try {
            if (!foundUser.next()) {
                incorrectParametersLabel.setText(Const.NO_SUCH_USER);
            } else {
                String salt = foundUser.getString(4);
                if (foundUser.getString(3).equals(hashClass.getSecurePasswordWithSalt(password, salt))) {

                    dbHandler.insertOnlineUserAfterAuth(user);
                    Scenes.MAIN_MENU.setScene((Stage) entranceButton.getScene().getWindow());
                } else incorrectParametersLabel.setText(Const.NO_SUCH_USER);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        entranceButton.setDisable(true);

        backButton.setOnAction(actionEvent -> {
            Scenes.MENU.setScene((Stage) backButton.getScene().getWindow());
        });

        recoverLoginButton.setOnAction(actionEvent -> {
            Scenes.RECOVER_LOGIN.setScene((Stage) recoverLoginButton.getScene().getWindow());
        });

        recoverPasswordButton.setOnAction(actionEvent -> {
            Scenes.RECOVER_PASSWORD.setScene((Stage) recoverPasswordButton.getScene().getWindow());
        });

        checkFieldsForFill(loginField);

        checkFieldsForFill(passField);

    }

    private void checkFieldsForFill(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() != 0) {
                if (checkForCorrectSymbols.isCorrectSymbolsForPasswordAndLogin(newValue)) {
                    field.setText(oldValue);
                }
                entranceButton.setDisable(loginField.getText().isEmpty() || passField.getText().isEmpty() || newValue.length()<6);
            } else entranceButton.setDisable(true);
        });
    }
}
