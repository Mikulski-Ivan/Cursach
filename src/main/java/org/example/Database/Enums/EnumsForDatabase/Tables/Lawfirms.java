package org.example.Database.Enums.EnumsForDatabase.Tables;

public enum Lawfirms {
    IDLAWFIRMS("idLawFirms"),
    FIRMNAME("firmName"),
    PHONE("phone"),
    ADDRESS("address");

    private String title;

    Lawfirms(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
