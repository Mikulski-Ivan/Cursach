package org.example.Database.Enums.EnumsForDatabase.Tables;

public enum Makers {
    IDMAKERS("idMakers"),
    MAKERFIRM("makerFirm"),
    REPUTATION("reputation"),
    YEARSOFCOOPERATION("yearsOfCooperation");

    private String title;

    Makers(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
