package org.example.Database.CursachN1;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForFX.Scenes;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class verificationForRegistrationControl implements Initializable {

    @FXML
    public Button backButton;

    @FXML
    private TextField codeField;

    @FXML
    private Button registrationButton;

    private final DatabaseHandler dbHandler = new DatabaseHandler();
    final methodsWithConnectionToInternet connection = new methodsWithConnectionToInternet();
    final String verificationCode = connection.getVerificationCode();
    final User user = new User();

    ResultSet onlineUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        onlineUser = dbHandler.getOnlineUser();

        registrationButton.setDisable(true);

        try {
            codeField.setDisable(!connection.isSentMessage(verificationCode, onlineUser.getString(5), Const.VERIFICATION_CODE));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        codeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() != 0) {
                if (checkForCorrectSymbols.isCorrectSymbolsForCode(newValue)) {
                    codeField.setText(oldValue);
                }
                registrationButton.setDisable(!newValue.equals(verificationCode));
            } else registrationButton.setDisable(true);
        });
    }

    public void registration() {

            dbHandler.registrationNewUser();
            try {
                user.setLogin(onlineUser.getString(2));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            dbHandler.insertOnlineIDAfterRegistration(user);


            try {
                connection.isSentMessage(Const.LINK_ACCOUNT_MESSAGE, onlineUser.getString(5), Const.LINK_ACCOUNT_THEME);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        Scenes.MAIN_MENU.setScene((Stage) registrationButton.getScene().getWindow());
    }

    public void back() {
        Scenes.MENU.setScene((Stage) backButton.getScene().getWindow());
    }
}
