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

import static org.example.Database.CursachN1.checkForCorrectSymbols.isCorrectSymbolsForPasswordAndLogin;

public class newLoginControl implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private Button exceptButton;

    @FXML
    private TextField newLoginField;

    @FXML
    private Label sameLoginField;


    private final DatabaseHandler dbHandler = new DatabaseHandler();
    final ResultSet onlineUser = dbHandler.getOnlineUser();
    final methodsWithConnectionToInternet connection = new methodsWithConnectionToInternet();


    public void back() {
        Scenes.SETTING.setScene((Stage) backButton.getScene().getWindow());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            checkFieldsForFill(exceptButton, newLoginField, onlineUser.getString(2));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkFieldsForFill(Button exceptButton, TextField newParameterField, String oldParameter) {

        exceptButton.setDisable(true);

        newParameterField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() != 0) {
                if (isCorrectSymbolsForPasswordAndLogin(newValue)) {
                    newParameterField.setText(oldValue);
                }
                exceptButton.setDisable(newParameterField.getText().equals(oldParameter) || newValue.length()<6);
            } else exceptButton.setDisable(true);
        });

        exceptButton.setOnAction(actionEvent -> except());
    }

    public void except() {
        User user = new User();
        user.setLogin(newLoginField.getText());

        try {
            if (dbHandler.findUser(user).next()) {
                sameLoginField.setText(Const.SUCH_USER_ALREADY_EXISTS);
            } else {


                String mail;
                try {
                    mail = onlineUser.getString(5);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                connection.isSentMessage(Const.LOGIN_WAS_CHANGED_MESSAGE, mail, Const.PARAMETER_WAS_CHANGED_THEME);

                try {
                    user.setID(onlineUser.getString(1));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                dbHandler.updateUserLogin(user);


                Scenes.MENU.setScene((Stage) backButton.getScene().getWindow());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
