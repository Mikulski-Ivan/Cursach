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
import java.sql.SQLException;
import java.util.ResourceBundle;

public class verificationForChangeLoginControl implements Initializable {

    @FXML
    private Label errorLabel;

    @FXML
    private Button verificationButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField codeField;

    @FXML
    private TextField passwordField;

    private final DatabaseHandler dbHandler = new DatabaseHandler();
    final methodsWithConnectionToInternet connection=new methodsWithConnectionToInternet();

    final String code = connection.getVerificationCode();
    String password;
    String salt;
    {
        try {
            password = dbHandler.getOnlineUser().getString(3);
            salt=dbHandler.getOnlineUser().getString(4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void back() {
        Scenes.MAIN_MENU.setScene((Stage) backButton.getScene().getWindow());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        verificationButton.setDisable(true);
        sentCode(dbHandler, connection, code, codeField, passwordField);

        codeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() != 0) {
                if (checkForCorrectSymbols.isCorrectSymbolsForCode(newValue)) {
                    codeField.setText(oldValue);
                }
                verificationButton.setDisable(codeField.getText().isEmpty() || passwordField.getText().isEmpty());
            } else verificationButton.setDisable(true);
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() != 0) {
                if (checkForCorrectSymbols.isCorrectSymbolsForPasswordAndLogin(newValue)) {
                    passwordField.setText(oldValue);
                }
                verificationButton.setDisable(codeField.getText().isEmpty() || passwordField.getText().isEmpty() || newValue.length()<6);
            } else verificationButton.setDisable(true);
        });
    }

    public void verification() {
        if (password.equals(hashClass.getSecurePasswordWithSalt(passwordField.getText(), salt)) && code.equals(codeField.getText())) {
            Scenes.NEW_LOGIN.setScene((Stage) verificationButton.getScene().getWindow());
        } else errorLabel.setText(Const.INCORRECT);
    }

    static void sentCode(DatabaseHandler dbHandler, methodsWithConnectionToInternet connection, String verificationCode, TextField codeField, TextField parameterField) {
        String mail = "";
        try {
            mail = dbHandler.getOnlineUser().getString(5);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String finalMail = mail;
        if (!connection.isSentMessage(verificationCode, finalMail, Const.VERIFICATION_CODE)) {
            codeField.setDisable(true);

            parameterField.setDisable(true);
        }
    }
}

