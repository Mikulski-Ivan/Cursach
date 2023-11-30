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

public class sentCodeForRecoverControl implements Initializable {

    @FXML
    public Button backButton;

    @FXML
    private TextField codeField;
    private final methodsWithConnectionToInternet connection = new methodsWithConnectionToInternet();
    private final String verificationCode = connection.getVerificationCode();
    private final DatabaseHandler dbHandler=new DatabaseHandler();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        backButton.setOnAction(actionEvent -> {
            Scenes.MENU.setScene((Stage) backButton.getScene().getWindow());
        });

        ResultSet onlineUser=dbHandler.getOnlineUser();

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
                if(newValue.equals(verificationCode)) {
                    switch (nullColumn(onlineUser)) {
                        case 2 -> Scenes.NEW_LOGIN.setScene((Stage) backButton.getScene().getWindow());
                        case 3 -> Scenes.NEW_PASSWORD.setScene((Stage) backButton.getScene().getWindow());
                    }
                }
            }
        });
    }

    private int nullColumn(ResultSet onlineUser) {
        for (int i = 1; i<5; ++i) {
            try {
                if (onlineUser.getString(i) != null && onlineUser.getString(i).equals(Const.recover)) {
                    return i;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }
}