package org.example.Database.Classes.ClassesForDatabase.Tables;

public class GoodTable {
    private int idGoods;
    private String goodsName;
    private Double costPerPiece;
    private int idMaker;
    private int leftInStock;

    public GoodTable(int idGoods, String goodsName, double costPerPiece, int idMaker, int leftInStock) {
        this.idGoods = idGoods;
        this.goodsName = goodsName;
        this.costPerPiece = costPerPiece;
        this.idMaker = idMaker;
        this.leftInStock = leftInStock;
    }

    public int getIdGoods() {
        return this.idGoods;
    }

    public void setIdGoods(int idGoods) {
        this.idGoods = idGoods;
    }

    public String getGoodsName() {
        return this.goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Double getCostPerPiece() {
        return this.costPerPiece;
    }

    public void setCostPerPiece(Double costPerPiece) {
        this.costPerPiece = costPerPiece;
    }

    public int getIdMaker() {
        return this.idMaker;
    }

    public void setIdMaker(int idMaker) {
        this.idMaker = idMaker;
    }

    public int getLeftInStock() {
        return this.leftInStock;
    }

    public void setLeftInStock(int leftInStock) {
        this.leftInStock = leftInStock;
    }
}
