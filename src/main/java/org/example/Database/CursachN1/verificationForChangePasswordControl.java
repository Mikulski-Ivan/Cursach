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

import static org.example.Database.CursachN1.checkForCorrectSymbols.isCorrectSymbolsForCode;
import static org.example.Database.CursachN1.checkForCorrectSymbols.isCorrectSymbolsForPasswordAndLogin;
import static org.example.Database.CursachN1.verificationForChangeLoginControl.sentCode;

public class verificationForChangePasswordControl implements Initializable {

    @FXML
    private Label errorLabel;

    @FXML
    private Button backButton;

    @FXML
    private TextField codeField;

    @FXML
    private TextField loginField;

    @FXML
    private Button verificationButton;

    private final DatabaseHandler dbHandler=new DatabaseHandler();
    private final methodsWithConnectionToInternet connection=new methodsWithConnectionToInternet();

    final String code = connection.getVerificationCode();
    String login;

    {
        try {
            login =dbHandler.getOnlineUser().getString(2);
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
        sentCode(dbHandler, connection, code, codeField, loginField);

        loginField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() != 0) {
                if (isCorrectSymbolsForPasswordAndLogin(newValue)) {
                    loginField.setText(oldValue);
                }
                verificationButton.setDisable(loginField.getText().isEmpty() || codeField.getText().isEmpty() || newValue.length()<6);
            } else verificationButton.setDisable(true);
        });

        codeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() != 0) {
                if (isCorrectSymbolsForCode(newValue)) {
                    codeField.setText(oldValue);
                }
                verificationButton.setDisable(loginField.getText().isEmpty() || codeField.getText().isEmpty());
            } else verificationButton.setDisable(true);
        });
    }

    public void verification() {
        if (login.equals(loginField.getText()) && code.equals(codeField.getText())) {
            Scenes.NEW_PASSWORD.setScene((Stage) verificationButton.getScene().getWindow());
        } else errorLabel.setText(Const.INCORRECT);
    }
}