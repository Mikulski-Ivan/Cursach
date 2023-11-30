package org.example.Database.CursachN1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForFX.Scenes;

import java.sql.SQLException;

import static java.lang.Integer.parseInt;

public class settingControl {

    @FXML
    private Button changeLoginButton;

    @FXML
    private Button backButton;

    @FXML
    private Button changeMailButton;

    @FXML
    private Button changePasswordButton;

    public void changeLogin() {
        Scenes.CHANGE_LOGIN.setScene((Stage) changeLoginButton.getScene().getWindow());
    }

    public void changePassword() {
        Scenes.CHANGE_PASSWORD.setScene((Stage) changePasswordButton.getScene().getWindow());
    }

    public void changeMail() {
        Scenes.CHANGE_MAIL.setScene((Stage) changeMailButton.getScene().getWindow());
    }

    public void back() throws SQLException {
        Scenes.MAIN_MENU.setScene((Stage) backButton.getScene().getWindow());
    }
}