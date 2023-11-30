package org.example.Database.Enums.EnumsForDatabase.Tables;

public enum Tables {
    WORKERS("workers"),
    LAWFIRMS("lawfirms"),
    LAWYERS("lawyers"),
    GOODS("goods"),
    MAKERS("makers"),
    CONTRACTS("contracts");

    private String title;

    Tables(String title) {
        this.title = title;
    }

    public String getTitle() {
        return '`'+title+'`';
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
