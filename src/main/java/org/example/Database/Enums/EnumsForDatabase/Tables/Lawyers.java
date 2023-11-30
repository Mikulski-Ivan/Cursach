package org.example.Database.Enums.EnumsForDatabase.Tables;

public enum Lawyers {
    IDLAWYERS("idLawyers"),
    FIO("FIO"),
    PHONE("phone"),
    IDLAWFIRMS("idLawFirm"),
    YEARSOFCOOPERATION("yearsOfCooperation");

    private String title;

    Lawyers(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
