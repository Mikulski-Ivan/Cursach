package org.example.Database.Enums.ConfigEnums;

public enum ServerConfigs {
    PATH("/approving"),
    LINK(" https://1ae4-46-216-113-216.ngrok-free.app"),
    GOAL("Approving");

    private final String title;

    ServerConfigs(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
