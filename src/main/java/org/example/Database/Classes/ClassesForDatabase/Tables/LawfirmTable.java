package org.example.Database.Classes.ClassesForDatabase.Tables;

public class LawfirmTable {
    private int idLawFirms;
    private String firmName;
    private String phone;
    private String address;

    public LawfirmTable(int idLawFirms, String firmName, String phone, String address) {
        this.idLawFirms = idLawFirms;
        this.firmName = firmName;
        this.phone = phone;
        this.address = address;
    }

    public LawfirmTable(String firmName, String phone, String address) {
        this.firmName = firmName;
        this.phone = phone;
        this.address = address;
    }
    public int getIdLawFirms() {
        return this.idLawFirms;
    }

    public void setIdLawFirms(int idLawFirms) {
        this.idLawFirms = idLawFirms;
    }

    public String getFirmName() {
        return this.firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
