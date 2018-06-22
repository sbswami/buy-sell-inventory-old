package com.sanshy.buysellinventory;

/**
 * Created by sbswami on 3/3/2018.
 */

public class stockitem {
    String productName;
    String quantity;
    String buyPrice;
    String sellPrice;


    public stockitem(){}

    public stockitem(String productName, String quantity, String buyPrice, String sellPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public stockitem(String productName, String buyPrice, String sellPrice) {
        this.productName = productName;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public String getProductName() {
        return productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public String getSellPrice() {
        return sellPrice;
    }
}
