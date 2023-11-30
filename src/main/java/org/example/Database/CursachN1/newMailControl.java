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

public class newMailControl implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private Button exceptButton;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField newMailField;

    final DatabaseHandler dbHandler = new DatabaseHandler();
    final ResultSet onlineUser = dbHandler.getOnlineUser();
    final String oldMail;

    {
        try {
            oldMail = onlineUser.getString(5);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    final methodsWithConnectionToInternet connection = new methodsWithConnectionToInternet();

    public void back() {
        Scenes.MENU.setScene((Stage) backButton.getScene().getWindow());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exceptButton.setDisable(true);

        newMailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() != 0) {
                if (checkForCorrectSymbols.isCorrectSymbolsForMail(newValue)) {
                    newMailField.setText(oldValue);
                }
                exceptButton.setDisable(newValue.equals(oldMail));
            } else exceptButton.setDisable(true);
        });

        exceptButton.setOnAction(actionEvent -> except());
    }

    public void except() {

        String newMail = newMailField.getText();
        boolean wasMessageSent = false;

        if (connection.isSentMessage(Const.RE_LINKED_TO_NEW_MAIL_MESSAGE_FIRST_PART + oldMail + Const.RE_LINKED_TO_NEW_MAIL_MESSAGE_SECOND_PART, newMail, Const.RE_LINKED_THEME)) {
            errorLabel.setText(Const.NO_CONNECTION_OR_INCORRECT_MAIL);
            wasMessageSent = true;
        }

        final boolean finalWasMessageSent = wasMessageSent;
        if (finalWasMessageSent) {
            connection.isSentMessage(Const.RE_LINKED_TO_OLD_MAIL_MESSAGE + newMail, oldMail, Const.RE_LINKED_THEME);
        }


        if (finalWasMessageSent) {
            User user = new User();
            try {
                user.setID(onlineUser.getString(1));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            user.setMail(newMail);
            dbHandler.updateUserMail(user);
        }

        if (wasMessageSent) {
            Scenes.MENU.setScene((Stage) backButton.getScene().getWindow());
        }
    }
}
