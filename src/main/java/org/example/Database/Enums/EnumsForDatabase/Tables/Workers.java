package org.example.Database.Enums.EnumsForDatabase.Tables;

public enum Workers {
    IDWORKERS("idWorkers"),
    FIO("FIO"),
    ADDRESS("address"),
    WORKERPOSITION("workerPosition"),
    SALARY("salary"),
    RATING("rating"),
    PHONE("phone"),
    EXPERIENCE("experience");

    private String title;

    Workers(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}