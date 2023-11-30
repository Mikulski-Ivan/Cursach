package org.example.Database.Controllers.MenuControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.Database.Enums.EnumsForFX.Scenes;

import java.net.URL;
import java.util.ResourceBundle;

public class TablesMenuController implements Initializable {

    @FXML
    private Button goodsButton;

    @FXML
    private Button workersButton;

    @FXML
    private Button contractsButton;

    @FXML
    private Button makersButton;

    @FXML
    private Button lawyersButton;

    @FXML
    private Button lawfirmsButton;
    @FXML
    private Button backButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnAction(actionEvent -> Scenes.MAIN_MENU.setScene((Stage) backButton.getScene().getWindow()));
        workersButton.setOnAction(actionEvent -> Scenes.WORKERS.setScene((Stage) workersButton.getScene().getWindow()));
        lawyersButton.setOnAction(actionEvent -> Scenes.LAWYERS.setScene((Stage) lawyersButton.getScene().getWindow()));
        lawfirmsButton.setOnAction(actionEvent -> Scenes.LAWFIRMS.setScene((Stage) lawfirmsButton.getScene().getWindow()));
        goodsButton.setOnAction(actionEvent -> Scenes.GOODS.setScene((Stage) goodsButton.getScene().getWindow()));
        makersButton.setOnAction(actionEvent -> Scenes.MAKERS.setScene((Stage) makersButton.getScene().getWindow()));
        contractsButton.setOnAction( actionEvent -> Scenes.CONTRACTS.setScene((Stage) contractsButton.getScene().getWindow()));
    }
}
