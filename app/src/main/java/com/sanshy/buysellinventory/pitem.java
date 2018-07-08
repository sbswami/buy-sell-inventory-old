package com.sanshy.buysellinventory;

/**
 * Created by sbswami on 3/2/2018.
 */

public class pitem {

    String name;
    String sellPrice;
    String buyPrice;
    String company;
    String companySellPrice;
    String companyBuyPrice;

    public pitem()
    {

    }

    public pitem(String name, String sellPrice, String buyPrice, String company) {
        this.name = name;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
        this.company = company;
    }

    public pitem(String name, String sellPrice, String buyPrice, String company, String companySellPrice, String companyBuyPrice) {
        this.name = name;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
        this.company = company;
        this.companySellPrice = companySellPrice;
        this.companyBuyPrice = companyBuyPrice;
    }

    public String getCompanySellPrice() {
        return companySellPrice;
    }

    public String getCompanyBuyPrice() {
        return companyBuyPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
