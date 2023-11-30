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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class verificationForChangeMailControl implements Initializable {

    @FXML
    private Button verificationButton;

    @FXML
    private Label errorLabel;

    @FXML
    private Button backButton;

    @FXML
    private TextField loginField;

    @FXML
    private TextField passwordField;

    private final DatabaseHandler dbHandler = new DatabaseHandler();

    private final ResultSet onlineUser=dbHandler.getOnlineUser();

    String login;
    String password;
    String salt;
    {
        try {
            password = onlineUser.getString(3);
            login=onlineUser.getString(2);
            salt=onlineUser.getString(4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void back() {
        Scenes.SETTING.setScene((Stage) backButton.getScene().getWindow());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        verificationButton.setDisable(true);

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() != 0) {
                if (checkForCorrectSymbols.isCorrectSymbolsForPasswordAndLogin(newValue)) {
                    passwordField.setText(oldValue);
                }
                verificationButton.setDisable(loginField.getText().isEmpty() || passwordField.getText().isEmpty() || newValue.length()<6);
            } else verificationButton.setDisable(true);
        });

        loginField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() != 0) {
                if (checkForCorrectSymbols.isCorrectSymbolsForPasswordAndLogin(newValue)) {
                   loginField.setText(oldValue);
                }
                verificationButton.setDisable(loginField.getText().isEmpty() || passwordField.getText().isEmpty() || newValue.length()<6);
            } else verificationButton.setDisable(true);
        });
    }

    public void verification() {
        if (password.equals(hashClass.getSecurePasswordWithSalt(passwordField.getText(), salt)) && login.equals(loginField.getText())) {
            Scenes.NEW_MAIL.setScene((Stage) verificationButton.getScene().getWindow());
        } else errorLabel.setText(Const.INCORRECT);
    }
}

