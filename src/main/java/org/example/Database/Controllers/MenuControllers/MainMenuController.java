package org.example.Database.Controllers.MenuControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.Database.Classes.HandlerClasses.DatabaseHandler;
import org.example.Database.Enums.EnumsForFX.Scenes;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    @FXML
    private Button tablesMenuButton;

    @FXML
    private Button backButton;

    @FXML
    private Button settingButton;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tablesMenuButton.setOnAction(actionEvent -> Scenes.TABLES_MENU.setScene((Stage) tablesMenuButton.getScene().getWindow()));
        backButton.setOnAction(actionEvent -> {
            Scenes.MENU.setScene((Stage) backButton.getScene().getWindow());
        });
        settingButton.setOnAction(actionEvent -> {
            Scenes.SETTING.setScene((Stage) settingButton.getScene().getWindow());
        });
    }
}