package org.example.Database.Classes.ClassesForDatabase.Tables;

public class LawyerTable {
    private int idLawyers;
    private String FIO;
    private String phone;
    private int idLawFirm;
    private int yearsOfCooperation;

    public LawyerTable(int idLawyers, String FIO, String phone, int idLawFirm, int yearsOfCooperation) {
        this.idLawyers = idLawyers;
        this.FIO = FIO;
        this.phone = phone;
        this.idLawFirm = idLawFirm;
        this.yearsOfCooperation = yearsOfCooperation;
    }

    public int getIdLawyers() {
        return this.idLawyers;
    }

    public void setIdLawyers(int idLawyers) {
        this.idLawyers = idLawyers;
    }

    public String getFIO() {
        return this.FIO;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIdLawFirm() {
        return this.idLawFirm;
    }

    public void setIdLawFirm(int idLawFirm) {
        this.idLawFirm = idLawFirm;
    }

    public int getYearsOfCooperation() {
        return this.yearsOfCooperation;
    }

    public void setYearsOfCooperation(int yearsOfCooperation) {
        this.yearsOfCooperation = yearsOfCooperation;
    }
}