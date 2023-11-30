package org.example.Database.Enums.EnumsForDatabase.Tables;

public enum Contracts {
    IDCONTRACTS("idContracts"),
    CUSTOMER("customer"),
    QUANTITY("quantity"),
    TOTALCOST("totalCost"),
    IDGOOD("idGood"),
    IDWORKER("idWorker"),
    IDLAWYER("idLawyer"),
    DATEOFSALE("dateOfSale");

    private String title;

    Contracts(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
