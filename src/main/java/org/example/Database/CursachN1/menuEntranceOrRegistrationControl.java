package org.example.Database.CursachN1;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForFX.Scenes;

import java.net.URL;
import java.util.ResourceBundle;

public class menuEntranceOrRegistrationControl implements Initializable {
    @FXML
    private Button entranceButton;
    @FXML
    private Button registrationButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        (new DatabaseHandler()).truncateOnlineUser();
        Tooltip t=new Tooltip("Нажмите");
        entranceButton.setTooltip(t);
        registrationButton.setTooltip(t);

        entranceButton.setOnAction(actionEvent -> Scenes.ENTRANCE.setScene((Stage) entranceButton.getScene().getWindow()));
        registrationButton.setOnAction(actionEvent -> Scenes.REGISTRATION.setScene((Stage) registrationButton.getScene().getWindow()));

    }
}
