package org.example.Database.Enums.EnumsForFX;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public enum Scenes {
    WORKERS("Tables/Workers.fxml"),
    MAIN_MENU("Menus/MainMenu.fxml"),
    TABLES_MENU("Menus/TablesMenu.fxml"),
    LAWYERS("Tables/Lawyers.fxml"),
    LAWFIRMS("Tables/Lawfirms.fxml"),
    GOODS("Tables/Goods.fxml"),
    MAKERS("Tables/Makers.fxml"),
    CONTRACTS("Tables/Contracts.fxml"),

    MENU("menuEntranceOrRegistration.fxml"),
    ENTRANCE("entrance.fxml"),
    REGISTRATION("registration.fxml"),
    RECOVER_LOGIN("recoverLogin.fxml"),
    RECOVER_PASSWORD("recoverPassword.fxml"),
    VERIFICATION("verificationForRegistration.fxml"),
    CHANGE_LOGIN("verificationForChangeLogin.fxml"),
    CHANGE_PASSWORD("verificationForChangePassword.fxml"),
    CHANGE_MAIL("verificationForChangeMail.fxml"),
    SENT_CODE("sentCodeForRecover.fxml"),
    SETTING("setting.fxml"),
    NEW_LOGIN("newLogin.fxml"),
    NEW_PASSWORD("newPassword.fxml"),
    NEW_MAIL("newMail.fxml");

    private String title;

    Scenes(String title) {
        this.title = title;
    }

    public String getTitle() {
        return "/org.example.Database/"+title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setScene(Stage stage) {
        Parent root=null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(this.getTitle())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(Objects.requireNonNull(root)));
    }
}
