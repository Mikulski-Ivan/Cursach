package org.example.Database.CursachN1;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForFX.Scenes;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class registrationControl implements Initializable {

    @FXML
    private Label errorLabel;

    @FXML
    private TextField mailField;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passField;

    @FXML
    private Button registrationButton;

    @FXML
    private PasswordField repeatPasswordField;

    @FXML
    private Button backButton;

    @FXML
    private CheckBox applicationBox;
    private final DatabaseHandler dbHandle=new DatabaseHandler();
    private final methodsWithConnectionToInternet connectionToInternet=new methodsWithConnectionToInternet();

    public void registration() {
        String salt=hashClass.getSalt();
        User user = new User();
        user.setLogin(loginField.getText());
        user.setPassword(hashClass.getSecurePasswordWithSalt(passField.getText(),salt));
        user.setSalt(salt);
        user.setMail(mailField.getText());
        try {
            if (!dbHandle.findUser(user).next()) {

                dbHandle.insertOnlineUserAfterRegistration(user);

                Scenes.VERIFICATION.setScene((Stage) registrationButton.getScene().getWindow());
            } else errorLabel.setText(Const.SUCH_USER_ALREADY_EXISTS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(actionEvent -> {
            Scenes.MENU.setScene((Stage) backButton.getScene().getWindow());
        });

        registrationButton.setDisable(true);

        checkFieldForFill(loginField);

        checkFieldForFill(passField);

        checkFieldForFill(repeatPasswordField);

        mailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() != 0) {
                if (checkForCorrectSymbols.isCorrectSymbolsForMail(newValue)) {
                    mailField.setText(oldValue);
                }

                disableRegistrationButton();

            } else registrationButton.setDisable(true);
        });
    }

    private void checkFieldForFill(TextField repeatPasswordField) {
        repeatPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() != 0) {
                if(checkForCorrectSymbols.isCorrectSymbolsForPasswordAndLogin(newValue)) {
                    repeatPasswordField.setText(oldValue);
                }

                disableRegistrationButton();


            } else registrationButton.setDisable(true);
        });
    }

    private void disableRegistrationButton() {
        registrationButton.setDisable(loginField.getText().isEmpty() ||
                passField.getText().isEmpty() ||
                repeatPasswordField.getText().isEmpty() ||
                mailField.getText().isEmpty() ||
                passField.getText().length()<6 ||
                loginField.getText().length()<6 ||
                !passField.getText().equals(repeatPasswordField.getText()));
    }
}
