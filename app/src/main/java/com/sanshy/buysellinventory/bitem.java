package com.sanshy.buysellinventory;

/**
 * Created by sbswami on 3/3/2018.
 */

public class bitem {

    String productName;
    String quantity;
    String price;
    String mode;
    String supplierName;
    String bid;
    String date;
    String date_supplierName;
    String date_productName;
    String modeProductName;
    String modeSupplierName;
    String date_mode;
    String productBuyPrice;
    String productSellPrice;

    public bitem()
    {

    }

    public bitem(String productName, String quantity, String price, String mode, String supplierName, String bid, String date, String date_supplierName, String date_productName, String modeProductName, String modeSupplierName, String date_mode, String productBuyPrice, String productSellPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.mode = mode;
        this.supplierName = supplierName;
        this.bid = bid;
        this.date = date;
        this.date_supplierName = date_supplierName;
        this.date_productName = date_productName;
        this.modeProductName = modeProductName;
        this.modeSupplierName = modeSupplierName;
        this.date_mode = date_mode;
        this.productBuyPrice = productBuyPrice;
        this.productSellPrice = productSellPrice;
    }

    public bitem(String productName, String quantity, String price, String mode, String supplierName, String bid, String date, String date_supplierName, String date_productName, String modeProductName, String modeSupplierName, String date_mode) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.mode = mode;
        this.supplierName = supplierName;
        this.bid = bid;
        this.date = date;
        this.date_supplierName = date_supplierName;
        this.date_productName = date_productName;
        this.modeProductName = modeProductName;
        this.modeSupplierName = modeSupplierName;
        this.date_mode = date_mode;
    }

    public bitem(String productName, String quantity, String price, String mode, String supplierName, String bid, String date, String date_supplierName, String date_productName, String modeProductName, String modeSupplierName) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.mode = mode;
        this.supplierName = supplierName;
        this.bid = bid;
        this.date = date;
        this.date_supplierName = date_supplierName;
        this.date_productName = date_productName;
        this.modeProductName = modeProductName;
        this.modeSupplierName = modeSupplierName;
    }

    public bitem(String productName, String quantity, String price, String mode, String supplierName, String bid, String date) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.mode = mode;
        this.supplierName = supplierName;
        this.bid = bid;
        this.date = date;
    }

    public bitem(String productName, String quantity, String price, String mode, String supplierName, String bid, String date, String date_supplierName, String date_productName) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.mode = mode;
        this.supplierName = supplierName;
        this.bid = bid;
        this.date = date;
        this.date_supplierName = date_supplierName;
        this.date_productName = date_productName;
    }

    public String getProductBuyPrice() {
        return productBuyPrice;
    }

    public String getProductSellPrice() {
        return productSellPrice;
    }

    public String getDate_mode() {
        return date_mode;
    }

    public String getModeProductName() {
        return modeProductName;
    }

    public String getModeSupplierName() {
        return modeSupplierName;
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

    public String getSupplierName() {
        return supplierName;
    }

    public String getBid() {
        return bid;
    }

    public String getDate() {
        return date;
    }

    public String getDate_supplierName() {
        return date_supplierName;
    }

    public String getDate_productName() {
        return date_productName;
    }
}
