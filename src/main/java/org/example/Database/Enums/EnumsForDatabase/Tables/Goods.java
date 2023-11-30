package org.example.Database.Enums.EnumsForDatabase.Tables;

public enum Goods {
    IDGOODS("idGoods"),
    GOODSNAME("goodsName"),
    COSTPERPIECE("costPerPiece"),
    IDMAKER("idMaker"),
    LEFTINSTOCK("leftInStock");

    private String title;

    Goods(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
