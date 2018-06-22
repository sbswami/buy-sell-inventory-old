package com.sanshy.buysellinventory;

/**
 * Created by sbswami on 3/3/2018.
 */

public class sellitem {

    String productName;
    String quantity;
    String price;
    String mode;
    String customerName;
    String sid;
    String date;
    String modeProductName;
    String modeCustomerName;
    String date_customerName;
    String date_productName;
    String date_mode;
    String productBuyPrice;
    String productSellPrice;

    public sellitem() {}

    public sellitem(String productName, String quantity, String price, String mode, String customerName, String sid, String date, String modeProductName, String modeCustomerName, String date_customerName, String date_productName, String date_mode, String productBuyPrice, String productSellPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.mode = mode;
        this.customerName = customerName;
        this.sid = sid;
        this.date = date;
        this.modeProductName = modeProductName;
        this.modeCustomerName = modeCustomerName;
        this.date_customerName = date_customerName;
        this.date_productName = date_productName;
        this.date_mode = date_mode;
        this.productBuyPrice = productBuyPrice;
        this.productSellPrice = productSellPrice;
    }

    public sellitem(String productName, String quantity, String price, String mode, String customerName, String sid, String date, String modeProductName, String modeCustomerName, String date_customerName, String date_productName, String date_mode) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.mode = mode;
        this.customerName = customerName;
        this.sid = sid;
        this.date = date;
        this.modeProductName = modeProductName;
        this.modeCustomerName = modeCustomerName;
        this.date_customerName = date_customerName;
        this.date_productName = date_productName;
        this.date_mode = date_mode;
    }

    public sellitem(String productName, String quantity, String price, String mode, String customerName, String sid, String date, String modeProductName) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.mode = mode;
        this.customerName = customerName;
        this.sid = sid;
        this.date = date;
        this.modeProductName = modeProductName;
    }

    public sellitem(String productName, String quantity, String price, String mode, String customerName, String sid, String date) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.mode = mode;
        this.customerName = customerName;
        this.sid = sid;
        this.date = date;
    }

    public String getProductBuyPrice() {
        return productBuyPrice;
    }

    public String getProductSellPrice() {
        return productSellPrice;
    }

    public String getDate_customerName() {
        return date_customerName;
    }

    public String getDate_productName() {
        return date_productName;
    }

    public String getDate_mode() {
        return date_mode;
    }

    public String getModeCustomerName() {
        return modeCustomerName;
    }

    public String getModeProductName() {
        return modeProductName;
    }

    public String getProductName() {
        return productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getMode() {
        return mode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getSid() {
        return sid;
    }

    public String getDate() {
        return date;
    }
}
