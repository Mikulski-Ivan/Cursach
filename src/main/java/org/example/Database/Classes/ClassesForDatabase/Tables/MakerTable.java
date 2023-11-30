package org.example.Database.Classes.ClassesForDatabase.Tables;

public class MakerTable {
    private int idMakers;
    private String  makerFirm;
    private Double reputation;
    private int yearsOfCooperation;

    public MakerTable(int idMakers, String makerFirm, Double reputation, int yearsOfCooperation) {
        this.idMakers = idMakers;
        this.makerFirm = makerFirm;
        this.reputation = reputation;
        this.yearsOfCooperation = yearsOfCooperation;
    }

    public int getIdMakers() {
        return this.idMakers;
    }

    public void setIdMakers(int idMakers) {
        this.idMakers = idMakers;
    }

    public String getMakerFirm() {
        return this.makerFirm;
    }

    public void setMakerFirm(String  makerFirm) {
        this.makerFirm = makerFirm;
    }

    public Double getReputation() {
        return this.reputation;
    }

    public void setReputation(Double reputation) {
        this.reputation = reputation;
    }

    public int getYearsOfCooperation() {
        return this.yearsOfCooperation;
    }

    public void setYearsOfCooperation(int yearsOfCooperation) {
        this.yearsOfCooperation = yearsOfCooperation;
    }
}
