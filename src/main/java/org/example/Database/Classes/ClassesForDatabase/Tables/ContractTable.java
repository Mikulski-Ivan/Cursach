package org.example.Database.Classes.ClassesForDatabase.Tables;

import java.sql.Date;

public class ContractTable {
    private int idContracts;
    private String customer;
    private int quantity;
    private Double totalCost;
    private int idGood;
    private int idWorker;
    private int idLawyer;
    private Date dateOfSale;

    public ContractTable(int idContracts, String customer, int quantity, double totalCost, int idGood, int idWorker, int idLawyer, Date dateOfSale) {
        this.idContracts = idContracts;
        this.customer = customer;
        this.quantity = quantity;
        this.totalCost = totalCost;
        this.idGood = idGood;
        this.idWorker = idWorker;
        this.idLawyer = idLawyer;
        this.dateOfSale = dateOfSale;
    }

    public int getIdContracts() {
        return this.idContracts;
    }

    public void setIdContracts(int idContracts) {
        this.idContracts = idContracts;
    }

    public String  getCustomer() {
        return this.customer;
    }

    public void setCustomer(String  customer) {
        this.customer = customer;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getTotalCost() {
        return this.totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public int getIdGood() {
        return this.idGood;
    }

    public void setIdGood(int idGood) {
        this.idGood = idGood;
    }

    public int getIdWorker() {
        return this.idWorker;
    }

    public void setIdWorker(int idWorker) {
        this.idWorker = idWorker;
    }

    public int getIdLawyer() {
        return this.idLawyer;
    }

    public void setIdLawyer(int idLawyer) {
        this.idLawyer = idLawyer;
    }

    public java.util.Date getDateOfSale() {
        return this.dateOfSale;
    }

    public void setDateOfSale(Date dateOfSale) {
        this.dateOfSale = dateOfSale;
    }
}
